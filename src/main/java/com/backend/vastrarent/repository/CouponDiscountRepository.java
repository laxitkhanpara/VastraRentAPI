package com.backend.vastrarent.repository;

import com.backend.vastrarent.model.CouponDiscount;
import com.backend.vastrarent.model.enums.StatusCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CouponDiscountRepository extends JpaRepository<CouponDiscount, Long> {

    Optional<CouponDiscount> findByCoupon(String coupon);

    List<CouponDiscount> findByStatusCoupon(StatusCoupon statusCoupon);

    List<CouponDiscount> findByValidTillAfterAndValidFromBefore(LocalDateTime now, LocalDateTime now1);

    List<CouponDiscount> findByUserId(Long userId);

    List<CouponDiscount> findByProductId(Long productId);
}