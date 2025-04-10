package com.backend.vastrarent.service;

import com.backend.vastrarent.dto.CouponDto.*;
import com.backend.vastrarent.model.enums.StatusCoupon;

import java.util.List;

public interface CouponDiscountService {

    CouponDiscountResponse createCoupon(CouponDiscountRequest request);

    CouponDiscountResponse updateCoupon(Long id, CouponDiscountRequest request);

    CouponDiscountResponse getCouponById(Long id);

    CouponDiscountResponse getCouponByCode(String code);

    List<CouponDiscountResponse> getAllCoupons();

    List<CouponDiscountResponse> getCouponsByStatus(StatusCoupon status);

    List<CouponDiscountResponse> getActiveCoupons();

    List<CouponDiscountResponse> getCouponsByUser(Long userId);

    List<CouponDiscountResponse> getCouponsByProduct(Long productId);

    void deleteCoupon(Long id);

    boolean validateCoupon(String couponCode);
}