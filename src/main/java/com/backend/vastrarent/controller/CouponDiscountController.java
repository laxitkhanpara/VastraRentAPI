package com.backend.vastrarent.controller;

import com.backend.vastrarent.dto.CouponDto.*;
import com.backend.vastrarent.model.enums.StatusCoupon;
import com.backend.vastrarent.service.CouponDiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CouponDiscountController {

    private final CouponDiscountService couponService;

    @PostMapping
    public ResponseEntity<CouponDiscountResponse> createCoupon(@RequestBody CouponDiscountRequest request) {
        return new ResponseEntity<>(couponService.createCoupon(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CouponDiscountResponse> updateCoupon(
            @PathVariable Long id,
            @RequestBody CouponDiscountRequest request) {
        return ResponseEntity.ok(couponService.updateCoupon(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CouponDiscountResponse> getCouponById(@PathVariable Long id) {
        return ResponseEntity.ok(couponService.getCouponById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<CouponDiscountResponse> getCouponByCode(@PathVariable String code) {
        return ResponseEntity.ok(couponService.getCouponByCode(code));
    }

    @GetMapping
    public ResponseEntity<List<CouponDiscountResponse>> getAllCoupons() {
        return ResponseEntity.ok(couponService.getAllCoupons());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<CouponDiscountResponse>> getCouponsByStatus(
            @PathVariable StatusCoupon status) {
        return ResponseEntity.ok(couponService.getCouponsByStatus(status));
    }

    @GetMapping("/active")
    public ResponseEntity<List<CouponDiscountResponse>> getActiveCoupons() {
        return ResponseEntity.ok(couponService.getActiveCoupons());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CouponDiscountResponse>> getCouponsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(couponService.getCouponsByUser(userId));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<CouponDiscountResponse>> getCouponsByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(couponService.getCouponsByProduct(productId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/validate/{code}")
    public ResponseEntity<Map<String, Boolean>> validateCoupon(@PathVariable String code) {
        boolean isValid = couponService.validateCoupon(code);
        return ResponseEntity.ok(Map.of("valid", isValid));
    }
}