package com.backend.vastrarent.mapper;

import com.backend.vastrarent.dto.OrderDto.OrderDTO;
import com.backend.vastrarent.dto.OrderDto.OrderRequest;
import com.backend.vastrarent.model.Order;
import com.backend.vastrarent.model.Product;
import com.backend.vastrarent.model.User;
import com.backend.vastrarent.model.enums.OrderStatus;
import com.backend.vastrarent.model.enums.PaymentStatus;
import com.backend.vastrarent.model.enums.RentalStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Component
public class OrderMapper {

    public Order toEntity(OrderRequest orderRequest, Product product, User renter, User owner) {
        LocalDate startDate = orderRequest.getRentalStartDate();
        LocalDate endDate = orderRequest.getRentalEndDate();
        int rentalDays = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;

        double rentalPrice = product.getRentalPrice() * orderRequest.getQuantity() * rentalDays;
        double securityDeposit = product.getSecurityDeposit() * orderRequest.getQuantity();
        double totalAmount = rentalPrice + securityDeposit;

        return Order.builder()
                .orderNumber(generateOrderNumber())
                .renter(renter)
                .owner(owner)
                .product(product)
                .quantity(orderRequest.getQuantity())
                .rentalPrice(rentalPrice)
                .securityDeposit(securityDeposit)
                .totalAmount(totalAmount)
                .rentalStartDate(startDate)
                .rentalEndDate(endDate)
                .rentalDuration(rentalDays)
                .orderStatus(OrderStatus.PENDING)
                .paymentStatus(PaymentStatus.PENDING)
                .rentalStatus(RentalStatus.PENDING)
                .isDepositReturned(false)
                .depositAmountReturned(0.0)
                .renterTerms(orderRequest.getRenterTerms())
                .ownerTermsAccepted(false)
                .renterTermsAccepted(false)
                .deliveryAddress(orderRequest.getDeliveryAddress())
                .deliveryCity(orderRequest.getDeliveryCity())
                .deliveryState(orderRequest.getDeliveryState())
                .deliveryPostalCode(orderRequest.getDeliveryPostalCode())
                .deliveryCountry(orderRequest.getDeliveryCountry())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public OrderDTO toDto(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .renterId(order.getRenter().getId())
                .renterName(order.getRenter().getFullName())
                .ownerId(order.getOwner().getId())
                .ownerName(order.getOwner().getFullName())
                .productId(order.getProduct().getId())
                .productTitle(order.getProduct().getTitle())
                .productImages(order.getProduct().getImageUrls())
                .quantity(order.getQuantity())
                .rentalPrice(order.getRentalPrice())
                .securityDeposit(order.getSecurityDeposit())
                .totalAmount(order.getTotalAmount())
                .rentalStartDate(order.getRentalStartDate())
                .rentalEndDate(order.getRentalEndDate())
                .rentalDuration(order.getRentalDuration())
                .orderStatus(order.getOrderStatus())
                .paymentStatus(order.getPaymentStatus())
                .rentalStatus(order.getRentalStatus())
                .isDepositReturned(order.isDepositReturned())
                .depositAmountReturned(order.getDepositAmountReturned())
                .depositReturnReason(order.getDepositReturnReason())
                .depositReturnDate(order.getDepositReturnDate())
                .ownerTerms(order.getOwnerTerms())
                .renterTerms(order.getRenterTerms())
                .ownerTermsAccepted(order.isOwnerTermsAccepted())
                .renterTermsAccepted(order.isRenterTermsAccepted())
                .damageReports(order.getDamageReports())
                .damageImages(order.getDamageImages())
                .cancellationReason(order.getCancellationReason())
                .cancellationDate(order.getCancellationDate())
                .deliveryAddress(order.getDeliveryAddress())
                .deliveryCity(order.getDeliveryCity())
                .deliveryState(order.getDeliveryState())
                .deliveryPostalCode(order.getDeliveryPostalCode())
                .deliveryCountry(order.getDeliveryCountry())
                .transactionId(order.getTransactionId())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}