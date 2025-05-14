package com.backend.vastrarent.model;

import com.backend.vastrarent.model.enums.OrderStatus;
import com.backend.vastrarent.model.enums.PaymentStatus;
import com.backend.vastrarent.model.enums.RentalStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "renter_id", nullable = false)
    private User renter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double rentalPrice;

    @Column(nullable = false)
    private double securityDeposit;

    @Column(nullable = false)
    private double totalAmount;

    @Column(nullable = false)
    private LocalDate rentalStartDate;

    @Column(nullable = false)
    private LocalDate rentalEndDate;

    @Column(nullable = false)
    private int rentalDuration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RentalStatus rentalStatus;

    private boolean isDepositReturned;

    private double depositAmountReturned;

    private String depositReturnReason;

    private LocalDateTime depositReturnDate;

    @Column(columnDefinition = "TEXT")
    private String ownerTerms;

    @Column(columnDefinition = "TEXT")
    private String renterTerms;

    private boolean ownerTermsAccepted;

    private boolean renterTermsAccepted;

    @ElementCollection
    @CollectionTable(name = "order_damage_reports", joinColumns = @JoinColumn(name = "order_id"))
    @Column(columnDefinition = "TEXT")
    private List<String> damageReports = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "order_damage_images", joinColumns = @JoinColumn(name = "order_id"))
    @Column(name = "image_url")
    private List<String> damageImages = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String cancellationReason;

    private LocalDateTime cancellationDate;
    @Column(nullable = false)
    private String deliveryAddress;
    @Column(nullable = false)
    private String deliveryCity;
    @Column(nullable = false)
    private String deliveryState;
    @Column(nullable = false)
    private String deliveryPostalCode;
    @Column(nullable = false)
    private String deliveryCountry;

    private String transactionId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}