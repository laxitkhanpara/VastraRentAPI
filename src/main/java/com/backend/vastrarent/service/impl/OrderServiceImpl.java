package com.backend.vastrarent.service.impl;

import com.backend.vastrarent.dto.OrderDto.*;
import com.backend.vastrarent.exception.ResourceNotFoundException;
import com.backend.vastrarent.exception.ValidationException;
import com.backend.vastrarent.mapper.OrderMapper;
import com.backend.vastrarent.model.Order;
import com.backend.vastrarent.model.Product;
import com.backend.vastrarent.model.User;
import com.backend.vastrarent.model.enums.OrderStatus;
import com.backend.vastrarent.model.enums.PaymentStatus;
import com.backend.vastrarent.model.enums.RentalStatus;
import com.backend.vastrarent.repository.OrderRepository;
import com.backend.vastrarent.repository.ProductRepository;
import com.backend.vastrarent.repository.UserRepository;
import com.backend.vastrarent.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderDTO createOrder(OrderRequest orderRequest, Long renterId) {
        // Get the renter
        User renter = userRepository.findById(renterId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + renterId));

        // Get the product
        Product product = productRepository.findById(orderRequest.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + orderRequest.getProductId()));

        // Get the owner
        User owner = product.getOwner();

        // Validate product availability
        if (!product.isAvailable()) {
            throw new ValidationException("Product is not available for rent");
        }

        // Validate dates
        if (orderRequest.getRentalEndDate().isBefore(orderRequest.getRentalStartDate())) {
            throw new ValidationException("Rental end date cannot be before rental start date");
        }

        // Check if product is available for requested dates
        if (!isProductAvailableForDates(product.getId(), orderRequest.getRentalStartDate(), orderRequest.getRentalEndDate())) {
            throw new ValidationException("Product is not available for the selected dates");
        }

        // Validate quantity
        if (orderRequest.getQuantity() > product.getQuantity()) {
            throw new ValidationException("Requested quantity exceeds available quantity");
        }

        // Create order entity
        Order order = orderMapper.toEntity(orderRequest, product, renter, owner);

        // Save order
        Order savedOrder = orderRepository.save(order);

        return orderMapper.toDto(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        // Validate user is owner or renter
        validateUserAccess(order, userId);

        return orderMapper.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderByOrderNumber(String orderNumber, Long userId) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with number: " + orderNumber));

        // Validate user is owner or renter
        validateUserAccess(order, userId);

        return orderMapper.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> getOrdersByRenterId(Long renterId, Pageable pageable) {
        // Validate user exists
        userRepository.findById(renterId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + renterId));

        return orderRepository.findByRenter_Id(renterId, pageable)
                .map(orderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> getOrdersByOwnerId(Long ownerId, Pageable pageable) {
        // Validate user exists
        userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + ownerId));

        return orderRepository.findByOwner_Id(ownerId, pageable)
                .map(orderMapper::toDto);
    }

    @Override
    @Transactional
    public OrderDTO updateOrderStatus(Long orderId, OrderStatus status, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        // Validate owner is updating the order
        if (!order.getOwner().getId().equals(userId)) {
            throw new ValidationException("Only the owner can update order status");
        }

        // Update status
        order.setOrderStatus(status);
        order.setUpdatedAt(LocalDateTime.now());

        // For specific status changes, update rental status as well
        if (status == OrderStatus.DELIVERED) {
            order.setRentalStatus(RentalStatus.ACTIVE);
        } else if (status == OrderStatus.COMPLETED) {
            order.setRentalStatus(RentalStatus.RETURNED_GOOD_CONDITION);
        } else if (status == OrderStatus.CANCELLED) {
            order.setRentalStatus(RentalStatus.CANCELLED);
        }

        // Save order
        Order updatedOrder = orderRepository.save(order);

        return orderMapper.toDto(updatedOrder);
    }

    @Override
    @Transactional
    public OrderDTO updatePaymentStatus(Long orderId, PaymentStatus status, String transactionId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        // Either renter or owner can update payment status (or admin in a real system)
        validateUserAccess(order, userId);

        // Update payment status
        order.setPaymentStatus(status);
        order.setTransactionId(transactionId);
        order.setUpdatedAt(LocalDateTime.now());

        // If payment is successful, update order status to CONFIRMED
        if (status == PaymentStatus.PAID && order.getOrderStatus() == OrderStatus.PENDING) {
            order.setOrderStatus(OrderStatus.CONFIRMED);
        }

        // Save order
        Order updatedOrder = orderRepository.save(order);

        return orderMapper.toDto(updatedOrder);
    }

    @Override
    @Transactional
    public OrderDTO updateRentalStatus(Long orderId, RentalStatus status, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        // Validate owner is updating the rental status
        if (!order.getOwner().getId().equals(userId)) {
            throw new ValidationException("Only the owner can update rental status");
        }

        // Update rental status
        order.setRentalStatus(status);
        order.setUpdatedAt(LocalDateTime.now());

        // Update order status based on rental status
        if (status == RentalStatus.RETURNED_GOOD_CONDITION || status == RentalStatus.RETURNED_WITH_DAMAGE) {
            order.setOrderStatus(OrderStatus.RETURNED);
        } else if (status == RentalStatus.LOST) {
            order.setOrderStatus(OrderStatus.DISPUTED);
        }

        // Save order
        Order updatedOrder = orderRepository.save(order);

        return orderMapper.toDto(updatedOrder);
    }

    @Override
    @Transactional
    public OrderDTO processDepositReturn(Long orderId, DepositReturnRequest request, Long ownerId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        // Validate owner is processing the deposit return
        if (!order.getOwner().getId().equals(ownerId)) {
            throw new ValidationException("Only the owner can process deposit returns");
        }

        // Validate order status is RETURNED or COMPLETED
        if (order.getOrderStatus() != OrderStatus.RETURNED && order.getOrderStatus() != OrderStatus.COMPLETED) {
            throw new ValidationException("Deposit can only be returned when order is in RETURNED or COMPLETED status");
        }

        // Validate return amount
        if (request.getReturnAmount() > order.getSecurityDeposit()) {
            throw new ValidationException("Return amount cannot exceed security deposit amount");
        }

        // Update deposit return information
        order.setDepositReturned(true);
        order.setDepositAmountReturned(request.getReturnAmount());
        order.setDepositReturnReason(request.getReturnReason());
        order.setDepositReturnDate(LocalDateTime.now());

        // If full deposit returned, update order status to COMPLETED
        if (request.getReturnAmount() == order.getSecurityDeposit() && order.getOrderStatus() == OrderStatus.RETURNED) {
            order.setOrderStatus(OrderStatus.COMPLETED);
        }

        // Save order
        Order updatedOrder = orderRepository.save(order);

        return orderMapper.toDto(updatedOrder);
    }

    @Override
    @Transactional
    public OrderDTO submitDamageReport(Long orderId, DamageReportRequest request, Long ownerId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        // Validate owner is submitting the damage report
        if (!order.getOwner().getId().equals(ownerId)) {
            throw new ValidationException("Only the owner can submit damage reports");
        }

        // Add damage report
        order.getDamageReports().add(request.getDescription());

        // Add damage images if provided
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            order.getDamageImages().addAll(request.getImageUrls());
        }

        // Update rental status to RETURNED_WITH_DAMAGE if currently ACTIVE
        if (order.getRentalStatus() == RentalStatus.ACTIVE) {
            order.setRentalStatus(RentalStatus.RETURNED_WITH_DAMAGE);
            order.setOrderStatus(OrderStatus.RETURNED);
        }

        // Save order
        Order updatedOrder = orderRepository.save(order);

        return orderMapper.toDto(updatedOrder);
    }

    @Override
    @Transactional
    public OrderDTO cancelOrder(Long orderId, String reason, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        // Validate user is owner or renter
        validateUserAccess(order, userId);

        // Validate order can be cancelled (not already completed or cancelled)
        if (order.getOrderStatus() == OrderStatus.COMPLETED ||
                order.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new ValidationException("Cannot cancel order with status: " + order.getOrderStatus());
        }

        // Update order status
        order.setOrderStatus(OrderStatus.CANCELLED);
        order.setRentalStatus(RentalStatus.CANCELLED);
        order.setCancellationReason(reason);
        order.setCancellationDate(LocalDateTime.now());

        // If payment was made, update payment status to REFUNDED
        if (order.getPaymentStatus() == PaymentStatus.PAID) {
            order.setPaymentStatus(PaymentStatus.REFUNDED);
        }

        // Make the product available again (if it was unavailable)
        Product product = order.getProduct();
        if (!product.isAvailable()) {
            product.setAvailable(true);
            productRepository.save(product);
        }

        // Save order
        Order updatedOrder = orderRepository.save(order);

        return orderMapper.toDto(updatedOrder);
    }

    @Override
    @Transactional
    public OrderDTO updateOwnerTerms(Long orderId, String terms, Long ownerId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        // Validate owner is updating terms
        if (!order.getOwner().getId().equals(ownerId)) {
            throw new ValidationException("Only the owner can update owner terms");
        }

        // Update owner terms
        order.setOwnerTerms(terms);
        order.setUpdatedAt(LocalDateTime.now());

        // Save order
        Order updatedOrder = orderRepository.save(order);

        return orderMapper.toDto(updatedOrder);
    }

    @Override
    @Transactional
    public OrderDTO acceptTerms(Long orderId, boolean isOwner, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if (isOwner) {
            // Validate user is owner
            if (!order.getOwner().getId().equals(userId)) {
                throw new ValidationException("Only the owner can accept owner terms");
            }

            // Update owner terms acceptance
            order.setOwnerTermsAccepted(true);
        } else {
            // Validate user is renter
            if (!order.getRenter().getId().equals(userId)) {
                throw new ValidationException("Only the renter can accept renter terms");
            }

            // Update renter terms acceptance
            order.setRenterTermsAccepted(true);
        }

        order.setUpdatedAt(LocalDateTime.now());

        // Save order
        Order updatedOrder = orderRepository.save(order);

        return orderMapper.toDto(updatedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isProductAvailableForDates(Long productId, LocalDate startDate, LocalDate endDate) {
        // Check if product exists
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        // Check if product is available for rent
        if (!product.isAvailable()) {
            return false;
        }

        // Check if the requested dates are within the product's availability window
        if (startDate.isBefore(product.getAvailableFrom()) || endDate.isAfter(product.getAvailableTill())) {
            return false;
        }

        // Check if there are no overlapping active orders
        List<OrderStatus> nonConflictingStatuses = Arrays.asList(
                OrderStatus.CANCELLED, OrderStatus.COMPLETED
        );

        List<Order> activeOrders = orderRepository.findByProduct_IdAndOrderStatusNotIn(productId, nonConflictingStatuses);

        // Check for date conflicts
        for (Order existingOrder : activeOrders) {
            // Skip if rental status is CANCELLED or RETURNED
            if (existingOrder.getRentalStatus() == RentalStatus.CANCELLED ||
                    existingOrder.getRentalStatus() == RentalStatus.RETURNED_GOOD_CONDITION ||
                    existingOrder.getRentalStatus() == RentalStatus.RETURNED_WITH_DAMAGE) {
                continue;
            }

            // Check for date overlap
            if (!(endDate.isBefore(existingOrder.getRentalStartDate()) ||
                    startDate.isAfter(existingOrder.getRentalEndDate()))) {
                return false;
            }
        }

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getActiveOrdersForProduct(Long productId) {
        // Check if product exists
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        List<OrderStatus> nonConflictingStatuses = Arrays.asList(
                OrderStatus.CANCELLED, OrderStatus.COMPLETED
        );

        List<Order> activeOrders = orderRepository.findByProduct_IdAndOrderStatusNotIn(productId, nonConflictingStatuses);

        return activeOrders.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void processOverdueRentals() {
        LocalDate today = LocalDate.now();
        List<Order> overdueRentals = orderRepository.findOverdueRentals(RentalStatus.ACTIVE, today);

        for (Order order : overdueRentals) {
            order.setRentalStatus(RentalStatus.OVERDUE);
            order.setUpdatedAt(LocalDateTime.now());
            orderRepository.save(order);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long getCompletedRentalsCount(Long userId) {
        return orderRepository.countCompletedRentalsForUser(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> filterOrdersByStatus(Long userId, boolean isOwner, OrderStatus status, Pageable pageable) {
        // Validate user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Page<Order> orders;
        if (isOwner) {
            orders = orderRepository.findByOwner_IdAndOrderStatus(userId, status, pageable);
        } else {
            orders = orderRepository.findByRenter_IdAndOrderStatus(userId, status, pageable);
        }

        return orders.map(orderMapper::toDto);
    }

    /**
     * Helper method to validate that the user is either the owner or renter of the order
     */
    private void validateUserAccess(Order order, Long userId) {
        if (!order.getRenter().getId().equals(userId) && !order.getOwner().getId().equals(userId)) {
            throw new ValidationException("User does not have access to this order");
        }
    }
}