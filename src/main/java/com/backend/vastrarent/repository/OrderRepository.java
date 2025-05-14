package com.backend.vastrarent.repository;

import com.backend.vastrarent.model.Order;
import com.backend.vastrarent.model.enums.OrderStatus;
import com.backend.vastrarent.model.enums.RentalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderNumber(String orderNumber);

    Page<Order> findByRenter_Id(Long renterId, Pageable pageable);

    Page<Order> findByOwner_Id(Long ownerId, Pageable pageable);

    Page<Order> findByProduct_Id(Long productId, Pageable pageable);

    Page<Order> findByRenter_IdAndOrderStatus(Long renterId, OrderStatus status, Pageable pageable);

    Page<Order> findByOwner_IdAndOrderStatus(Long ownerId, OrderStatus status, Pageable pageable);

    List<Order> findByProduct_IdAndOrderStatusNotIn(Long productId, List<OrderStatus> statuses);

    @Query("SELECT o FROM Order o WHERE o.rentalStatus = :status AND o.rentalEndDate < :today")
    List<Order> findOverdueRentals(@Param("status") RentalStatus status, @Param("today") LocalDate today);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.renter.id = :userId AND o.orderStatus = 'COMPLETED'")
    Long countCompletedRentalsForUser(@Param("userId") Long userId);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.owner.id = :ownerId AND o.product.id = :productId AND o.orderStatus = 'COMPLETED'")
    Long countCompletedRentalsForProductAndOwner(@Param("ownerId") Long ownerId, @Param("productId") Long productId);

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END FROM Order o " +
            "WHERE o.product.id = :productId AND o.rentalStatus = 'ACTIVE' AND " +
            "((o.rentalStartDate BETWEEN :startDate AND :endDate) OR " +
            "(o.rentalEndDate BETWEEN :startDate AND :endDate) OR " +
            "(:startDate BETWEEN o.rentalStartDate AND o.rentalEndDate) OR " +
            "(:endDate BETWEEN o.rentalStartDate AND o.rentalEndDate))")
    boolean isProductAvailableForDates(@Param("productId") Long productId,
                                       @Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate);
}