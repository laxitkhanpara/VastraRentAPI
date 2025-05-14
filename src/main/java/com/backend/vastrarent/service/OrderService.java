package com.backend.vastrarent.service;

import com.backend.vastrarent.dto.OrderDto.*;
import com.backend.vastrarent.model.enums.OrderStatus;
import com.backend.vastrarent.model.enums.PaymentStatus;
import com.backend.vastrarent.model.enums.RentalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {

    /**
     * Create a new order
     */
    @Transactional
    OrderDTO createOrder(OrderRequest orderRequest, Long renterId);

    /**
     * Get an order by ID
     */
    @Transactional(readOnly = true)
    OrderDTO getOrderById(Long orderId, Long userId);

    /**
     * Get an order by order number
     */
    @Transactional(readOnly = true)
    OrderDTO getOrderByOrderNumber(String orderNumber, Long userId);

    /**
     * Get orders for a renter
     */
    @Transactional(readOnly = true)
    Page<OrderDTO> getOrdersByRenterId(Long renterId, Pageable pageable);

    /**
     * Get orders for an owner
     */
    @Transactional(readOnly = true)
    Page<OrderDTO> getOrdersByOwnerId(Long ownerId, Pageable pageable);

    /**
     * Update order status
     */
    @Transactional
    OrderDTO updateOrderStatus(Long orderId, OrderStatus status, Long userId);

    /**
     * Update payment status
     */
    @Transactional
    OrderDTO updatePaymentStatus(Long orderId, PaymentStatus status, String transactionId, Long userId);

    /**
     * Update rental status
     */
    @Transactional
    OrderDTO updateRentalStatus(Long orderId, RentalStatus status, Long userId);

    /**
     * Process security deposit return
     */
    @Transactional
    OrderDTO processDepositReturn(Long orderId, DepositReturnRequest request, Long ownerId);

    /**
     * Submit damage report
     */
    @Transactional
    OrderDTO submitDamageReport(Long orderId, DamageReportRequest request, Long ownerId);

    /**
     * Cancel order
     */
    @Transactional
    OrderDTO cancelOrder(Long orderId, String reason, Long userId);

    /**
     * Update owner terms
     */
    @Transactional
    OrderDTO updateOwnerTerms(Long orderId, String terms, Long ownerId);

    /**
     * Accept terms (by owner or renter)
     */
    @Transactional
    OrderDTO acceptTerms(Long orderId, boolean isOwner, Long userId);

    /**
     * Check if product is available for the specified dates
     */
    @Transactional(readOnly = true)
    boolean isProductAvailableForDates(Long productId, LocalDate startDate, LocalDate endDate);

    /**
     * Get active orders for a product
     */
    @Transactional(readOnly = true)
    List<OrderDTO> getActiveOrdersForProduct(Long productId);

    /**
     * Process overdue rentals automatically
     */
    @Transactional
    void processOverdueRentals();

    /**
     * Get order history statistics for a user
     */
    @Transactional(readOnly = true)
    long getCompletedRentalsCount(Long userId);

    /**
     * Filter orders by status for a specific user
     */
    @Transactional(readOnly = true)
    Page<OrderDTO> filterOrdersByStatus(Long userId, boolean isOwner, OrderStatus status, Pageable pageable);
}