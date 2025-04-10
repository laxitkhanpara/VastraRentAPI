package com.backend.vastrarent.model;


import com.backend.vastrarent.model.enums.DiscountType;
import com.backend.vastrarent.model.enums.StatusCoupon;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "coupondiscount")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @Column(nullable = false, name = "discountType")
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @Column(nullable = true)
    private BigDecimal amount;

    @Column(nullable = true)
    private double percent;

    @Column(nullable = true)
    private Integer buy;

    @Column(nullable = true)
    private Integer get;

    private String coupon;

    //staus
    @Enumerated(EnumType.STRING)
    @Column(name = "status_coupon")
    private StatusCoupon statusCoupon;
    private LocalDateTime validFrom;
    private LocalDateTime validTill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}
