package com.backend.vastrarent.repository;

import com.backend.vastrarent.model.Product;
import com.backend.vastrarent.model.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByOwner_Id(Long ownerId);

    Page<Product> findByIsAvailableTrue(Pageable pageable);

    @Query(value = "SELECT * FROM products p WHERE p.is_available = true " +
            "AND (:category IS NULL OR :category = ANY(p.category)) " +
            "AND (:city IS NULL OR p.city = :city) " +
            "AND (:minPrice IS NULL OR p.rental_price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.rental_price <= :maxPrice) " +
            "AND (:availableFrom IS NULL OR p.available_till >= :availableFrom) " +
            "AND (:availableTill IS NULL OR p.available_from <= :availableTill)",
            countQuery = "SELECT COUNT(*) FROM products p WHERE p.is_available = true " +
                    "AND (:category IS NULL OR :category = ANY(p.category)) " +
                    "AND (:city IS NULL OR p.city = :city) " +
                    "AND (:minPrice IS NULL OR p.rental_price >= :minPrice) " +
                    "AND (:maxPrice IS NULL OR p.rental_price <= :maxPrice) " +
                    "AND (:availableFrom IS NULL OR p.available_till >= :availableFrom) " +
                    "AND (:availableTill IS NULL OR p.available_from <= :availableTill)",
            nativeQuery = true)
    Page<Product> searchProducts(
            @Param("category") String category,
            @Param("city") String city,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("availableFrom") LocalDate availableFrom,
            @Param("availableTill") LocalDate availableTill,
            Pageable pageable);

    List<Product> findByStatusAndIsAvailableTrue(Status status);

    @Query("SELECT p FROM Product p WHERE p.isAvailable = true AND " +
            "p.availableFrom <= :date AND p.availableTill >= :date")
    List<Product> findAvailableProductsForDate(@Param("date") LocalDate date);

    List<Product> findTop10ByOrderByViewsDesc();

    List<Product> findByTitleContainingIgnoreCase(String keyword);
}