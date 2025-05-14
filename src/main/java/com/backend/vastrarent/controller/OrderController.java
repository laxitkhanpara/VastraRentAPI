package com.backend.vastrarent.controller;

import com.backend.vastrarent.dto.OrderDto.*;
import com.backend.vastrarent.model.UserPrincipal;
import com.backend.vastrarent.model.enums.OrderStatus;
import com.backend.vastrarent.model.enums.PaymentStatus;
import com.backend.vastrarent.model.enums.RentalStatus;
import com.backend.vastrarent.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest orderRequest,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = getUserIdFromUserDetails(userPrincipal);
        OrderDTO createdOrder = orderService.createOrder(orderRequest, userId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new OrderResponse("success", "Order created successfully", createdOrder));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long orderId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = getUserIdFromUserDetails(userPrincipal);
        OrderDTO order = orderService.getOrderById(orderId, userId);

        return ResponseEntity.ok(new OrderResponse("success", "Order fetched successfully", order));
    }

    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<OrderResponse> getOrderByOrderNumber(
            @PathVariable String orderNumber,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = getUserIdFromUserDetails(userPrincipal);
        OrderDTO order = orderService.getOrderByOrderNumber(orderNumber, userId);

        return ResponseEntity.ok(new OrderResponse("success", "Order fetched successfully", order));
    }

    @GetMapping("/my-rentals")
    public ResponseEntity<OrderResponse> getMyRentals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = getUserIdFromUserDetails(userPrincipal);
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<OrderDTO> orders = orderService.getOrdersByRenterId(userId, pageable);

        return ResponseEntity.ok(new OrderResponse("success", "Your rental orders fetched successfully", orders));
    }

    @GetMapping("/my-rentals/filter")
    public ResponseEntity<OrderResponse> filterMyRentals(
            @RequestParam OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = getUserIdFromUserDetails(userPrincipal);
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<OrderDTO> orders = orderService.filterOrdersByStatus(userId, false, status, pageable);

        return ResponseEntity.ok(new OrderResponse("success", "Filtered rental orders fetched successfully", orders));
    }

    @GetMapping("/my-listings")
    public ResponseEntity<OrderResponse> getMyListings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = getUserIdFromUserDetails(userPrincipal);
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<OrderDTO> orders = orderService.getOrdersByOwnerId(userId, pageable);

        return ResponseEntity.ok(new OrderResponse("success", "Your listed item orders fetched successfully", orders));
    }

    @GetMapping("/my-listings/filter")
    public ResponseEntity<OrderResponse> filterMyListings(
            @RequestParam OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = getUserIdFromUserDetails(userPrincipal);
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<OrderDTO> orders = orderService.filterOrdersByStatus(userId, true, status, pageable);

        return ResponseEntity.ok(new OrderResponse("success", "Filtered listing orders fetched successfully", orders));
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = getUserIdFromUserDetails(userPrincipal);
        OrderDTO updatedOrder = orderService.updateOrderStatus(orderId, status, userId);

        return ResponseEntity.ok(new OrderResponse("success", "Order status updated successfully", updatedOrder));
    }

    @PatchMapping("/{orderId}/payment")
    public ResponseEntity<OrderResponse> updatePaymentStatus(
            @PathVariable Long orderId,
            @RequestParam PaymentStatus status,
            @RequestParam(required = false) String transactionId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = getUserIdFromUserDetails(userPrincipal);
        OrderDTO updatedOrder = orderService.updatePaymentStatus(orderId, status, transactionId, userId);

        return ResponseEntity.ok(new OrderResponse("success", "Payment status updated successfully", updatedOrder));
    }

    @PatchMapping("/{orderId}/rental")
    public ResponseEntity<OrderResponse> updateRentalStatus(
            @PathVariable Long orderId,
            @RequestParam RentalStatus status,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = getUserIdFromUserDetails(userPrincipal);
        OrderDTO updatedOrder = orderService.updateRentalStatus(orderId, status, userId);

        return ResponseEntity.ok(new OrderResponse("success", "Rental status updated successfully", updatedOrder));
    }

    @PostMapping("/{orderId}/deposit-return")
    public ResponseEntity<OrderResponse> processDepositReturn(
            @PathVariable Long orderId,
            @Valid @RequestBody DepositReturnRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = getUserIdFromUserDetails(userPrincipal);
        OrderDTO updatedOrder = orderService.processDepositReturn(orderId, request, userId);

        return ResponseEntity.ok(new OrderResponse("success", "Security deposit return processed successfully", updatedOrder));
    }

    @PostMapping("/{orderId}/damage-report")
    public ResponseEntity<OrderResponse> submitDamageReport(
            @PathVariable Long orderId,
            @Valid @RequestBody DamageReportRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = getUserIdFromUserDetails(userPrincipal);
        OrderDTO updatedOrder = orderService.submitDamageReport(orderId, request, userId);

        return ResponseEntity.ok(new OrderResponse("success", "Damage report submitted successfully", updatedOrder));
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable Long orderId,
            @RequestParam String reason,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = getUserIdFromUserDetails(userPrincipal);
        OrderDTO cancelledOrder = orderService.cancelOrder(orderId, reason, userId);

        return ResponseEntity.ok(new OrderResponse("success", "Order cancelled successfully", cancelledOrder));
    }

    @PatchMapping("/{orderId}/owner-terms")
    public ResponseEntity<OrderResponse> updateOwnerTerms(
            @PathVariable Long orderId,
            @RequestParam String terms,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = getUserIdFromUserDetails(userPrincipal);
        OrderDTO updatedOrder = orderService.updateOwnerTerms(orderId, terms, userId);

        return ResponseEntity.ok(new OrderResponse("success", "Owner terms updated successfully", updatedOrder));
    }

    @PostMapping("/{orderId}/accept-terms")
    public ResponseEntity<OrderResponse> acceptTerms(
            @PathVariable Long orderId,
            @RequestParam boolean isOwner,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = getUserIdFromUserDetails(userPrincipal);
        OrderDTO updatedOrder = orderService.acceptTerms(orderId, isOwner, userId);

        return ResponseEntity.ok(new OrderResponse("success", "Terms accepted successfully", updatedOrder));
    }

    @GetMapping("/product/{productId}/availability")
    public ResponseEntity<OrderResponse> checkProductAvailability(
            @PathVariable Long productId,
            @RequestParam String startDate,
            @RequestParam String endDate) {

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        boolean isAvailable = orderService.isProductAvailableForDates(productId, start, end);

        return ResponseEntity.ok(new OrderResponse("success", "Product availability checked successfully", isAvailable));
    }

    @GetMapping("/product/{productId}/active")
    public ResponseEntity<OrderResponse> getActiveOrdersForProduct(
            @PathVariable Long productId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        List<OrderDTO> activeOrders = orderService.getActiveOrdersForProduct(productId);

        return ResponseEntity.ok(new OrderResponse("success", "Active orders for product fetched successfully", activeOrders));
    }

    @GetMapping("/stats/completed")
    public ResponseEntity<OrderResponse> getCompletedRentalsCount(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = getUserIdFromUserDetails(userPrincipal);
        long count = orderService.getCompletedRentalsCount(userId);

        return ResponseEntity.ok(new OrderResponse("success", "Completed rentals count fetched successfully", count));
    }

    private Long getUserIdFromUserDetails(UserPrincipal userPrincipal) {
        // Extract user ID from UserPrincipal object
        return userPrincipal.getId();
    }
}