package com.backend.vastrarent.service.impl;

import com.backend.vastrarent.dto.CouponDto.*;
import com.backend.vastrarent.exception.ResourceNotFoundException;
import com.backend.vastrarent.model.CouponDiscount;
import com.backend.vastrarent.model.Product;
import com.backend.vastrarent.model.User;
import com.backend.vastrarent.model.enums.DiscountType;
import com.backend.vastrarent.model.enums.StatusCoupon;
import com.backend.vastrarent.repository.CouponDiscountRepository;
import com.backend.vastrarent.repository.ProductRepository;
import com.backend.vastrarent.repository.UserRepository;
import com.backend.vastrarent.service.CouponDiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponDiscountServiceImpl implements CouponDiscountService {

    private final CouponDiscountRepository couponRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public CouponDiscountResponse createCoupon(CouponDiscountRequest request) {
        validateCouponRequest(request);

        CouponDiscount coupon = mapToEntity(request);
        CouponDiscount savedCoupon = couponRepository.save(coupon);
        return mapToResponse(savedCoupon);
    }

    @Override
    public CouponDiscountResponse updateCoupon(Long id, CouponDiscountRequest request) {
        CouponDiscount existingCoupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with id: " + id));

        validateCouponRequest(request);

        // Update fields
        updateCouponEntity(existingCoupon, request);

        CouponDiscount updatedCoupon = couponRepository.save(existingCoupon);
        return mapToResponse(updatedCoupon);
    }

    @Override
    public CouponDiscountResponse getCouponById(Long id) {
        CouponDiscount coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with id: " + id));
        return mapToResponse(coupon);
    }

    @Override
    public CouponDiscountResponse getCouponByCode(String code) {
        CouponDiscount coupon = couponRepository.findByCoupon(code)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with code: " + code));
        return mapToResponse(coupon);
    }

    @Override
    public List<CouponDiscountResponse> getAllCoupons() {
        return couponRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CouponDiscountResponse> getCouponsByStatus(StatusCoupon status) {
        return couponRepository.findByStatusCoupon(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CouponDiscountResponse> getActiveCoupons() {
        LocalDateTime now = LocalDateTime.now();
        return couponRepository.findByValidTillAfterAndValidFromBefore(now, now).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CouponDiscountResponse> getCouponsByUser(Long userId) {
        return couponRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CouponDiscountResponse> getCouponsByProduct(Long productId) {
        return couponRepository.findByProductId(productId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCoupon(Long id) {
        if (!couponRepository.existsById(id)) {
            throw new ResourceNotFoundException("Coupon not found with id: " + id);
        }
        couponRepository.deleteById(id);
    }

    @Override
    public boolean validateCoupon(String couponCode) {
        return couponRepository.findByCoupon(couponCode)
                .filter(coupon -> {
                    LocalDateTime now = LocalDateTime.now();
                    return coupon.getStatusCoupon() == StatusCoupon.ACTIVE &&
                            coupon.getValidFrom().isBefore(now) &&
                            coupon.getValidTill().isAfter(now);
                })
                .isPresent();
    }

    private CouponDiscount mapToEntity(CouponDiscountRequest request) {
        CouponDiscount coupon = new CouponDiscount();

        coupon.setDescription(request.getDescription());
        coupon.setDiscountType(request.getDiscountType());
        coupon.setCoupon(request.getCoupon());
        coupon.setStatusCoupon(request.getStatusCoupon());
        coupon.setValidFrom(request.getValidFrom());
        coupon.setValidTill(request.getValidTill());

        // Set type-specific fields
        if (request.getDiscountType() == DiscountType.FLATAMOUNT) {
            coupon.setAmount(request.getAmount());
        } else if (request.getDiscountType() == DiscountType.PERCENT) {
            coupon.setPercent(request.getPercent());
        } else if (request.getDiscountType() == DiscountType.BUTGET) {
            coupon.setBuy(request.getBuy());
            coupon.setGet(request.getGet());
        }

        // Set user if provided
        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));
            coupon.setUser(user);
        }

        // Set product if provided
        if (request.getProductId() != null) {
            Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));
            coupon.setProduct(product);
        }

        return coupon;
    }

    private void updateCouponEntity(CouponDiscount coupon, CouponDiscountRequest request) {
        coupon.setDescription(request.getDescription());
        coupon.setDiscountType(request.getDiscountType());
        coupon.setCoupon(request.getCoupon());
        coupon.setStatusCoupon(request.getStatusCoupon());
        coupon.setValidFrom(request.getValidFrom());
        coupon.setValidTill(request.getValidTill());

        // Reset type-specific fields
        coupon.setAmount(null);
        coupon.setPercent(0);
        coupon.setBuy(null);
        coupon.setGet(null);

        // Set type-specific fields
        if (request.getDiscountType() == DiscountType.FLATAMOUNT) {
            coupon.setAmount(request.getAmount());
        } else if (request.getDiscountType() == DiscountType.PERCENT) {
            coupon.setPercent(request.getPercent());
        } else if (request.getDiscountType() == DiscountType.BUTGET) {
            coupon.setBuy(request.getBuy());
            coupon.setGet(request.getGet());
        }

        // Update user if provided
        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));
            coupon.setUser(user);
        } else {
            coupon.setUser(null);
        }

        // Update product if provided
        if (request.getProductId() != null) {
            Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));
            coupon.setProduct(product);
        } else {
            coupon.setProduct(null);
        }
    }

    private CouponDiscountResponse mapToResponse(CouponDiscount coupon) {
        return CouponDiscountResponse.builder()
                .id(coupon.getId())
                .description(coupon.getDescription())
                .discountType(coupon.getDiscountType())
                .amount(coupon.getAmount())
                .percent(coupon.getPercent())
                .buy(coupon.getBuy())
                .get(coupon.getGet())
                .coupon(coupon.getCoupon())
                .statusCoupon(coupon.getStatusCoupon())
                .validFrom(coupon.getValidFrom())
                .validTill(coupon.getValidTill())
                .userId(coupon.getUser() != null ? coupon.getUser().getId() : null)
                .productId(coupon.getProduct() != null ? coupon.getProduct().getId() : null)
                .build();
    }

    private void validateCouponRequest(CouponDiscountRequest request) {
        if (request.getDiscountType() == null) {
            throw new IllegalArgumentException("Discount type is required");
        }

        // Validate type-specific required fields
        switch (request.getDiscountType()) {
            case FLATAMOUNT:
                if (request.getAmount() == null) {
                    throw new IllegalArgumentException("Amount is required for FLATAMOUNT discount type");
                }
                break;
            case PERCENT:
                if (request.getPercent() == null) {
                    throw new IllegalArgumentException("Percent is required for PERCENT discount type");
                }
                if (request.getPercent() < 0 || request.getPercent() > 100) {
                    throw new IllegalArgumentException("Percent must be between 0 and 100");
                }
                break;
            case BUTGET:
                if (request.getBuy() == null || request.getGet() == null) {
                    throw new IllegalArgumentException("Buy and Get quantities are required for BUTGET discount type");
                }
                if (request.getBuy() <= 0 || request.getGet() <= 0) {
                    throw new IllegalArgumentException("Buy and Get quantities must be positive");
                }
                break;
        }

        // Validate coupon code
        if (request.getCoupon() == null || request.getCoupon().trim().isEmpty()) {
            throw new IllegalArgumentException("Coupon code is required");
        }

        // Validate dates
        if (request.getValidFrom() == null || request.getValidTill() == null) {
            throw new IllegalArgumentException("Valid from and valid till dates are required");
        }

        if (request.getValidFrom().isAfter(request.getValidTill())) {
            throw new IllegalArgumentException("Valid from date must be before valid till date");
        }
    }
}