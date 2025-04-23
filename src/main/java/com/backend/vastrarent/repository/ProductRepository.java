package com.backend.vastrarent.repository;

import com.backend.vastrarent.model.Product;
import com.backend.vastrarent.model.User;
import com.backend.vastrarent.model.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>,JpaSpecificationExecutor<Product>{

    List<Product> findByOwner_Id(Long ownerId);

    Page<Product> findByIsAvailableTrue(Pageable pageable);
/*
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
            Pageable pageable);*/

    List<Product> findByStatusAndIsAvailableTrue(Status status);

    // Find products within distance (in meters) from the given point
    @Query(value = "SELECT p.* FROM products p " +
            "WHERE ST_DWithin(p.location, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography, :distance) " +
            "AND p.is_available = true " +
            "ORDER BY ST_Distance(p.location, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography)",
            nativeQuery = true)
    Page<Product> findNearbyProducts(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("distance") double distance,
            Pageable pageable);


    List<Product> findTop10ByOrderByViewsDesc();

    List<Product> findByTitleContainingIgnoreCase(String keyword);

/*
    @Query(value =
            "SELECT p.* FROM products p " +
                    "WHERE (:categories IS NULL OR cardinality(:categories) = 0 OR CAST(p.category AS text[]) && CAST(:categories AS text[])) " +
                    "AND (:sizes IS NULL OR cardinality(:sizes) = 0 OR CAST(p.size AS text[]) && CAST(:sizes AS text[])) " +
                    "AND (:colors IS NULL OR cardinality(:colors) = 0 OR CAST(p.color AS text[]) && CAST(:colors AS text[])) " +
                    "AND (:styleTags IS NULL OR cardinality(:styleTags) = 0 OR CAST(p.style_tags AS text[]) && CAST(:styleTags AS text[])) " +
                    "AND (:condition IS NULL OR p.condition = :condition) " +
                    "AND (:minRentalPrice IS NULL OR p.rental_price >= :minRentalPrice) " +
                    "AND (:maxRentalPrice IS NULL OR p.rental_price <= :maxRentalPrice) " +
                    "AND (:minDeposit IS NULL OR p.security_deposit >= :minDeposit) " +
                    "AND (:maxDeposit IS NULL OR p.security_deposit <= :maxDeposit) " +
                    "AND (:availableFrom IS NULL OR p.available_till >= :availableFrom) " +
                    "AND (:availableTill IS NULL OR p.available_from <= :availableTill) " +
                    "AND ((:latitude IS NULL OR :longitude IS NULL OR :distance IS NULL) OR " +
                    "ST_DWithin(p.location, ST_SetSRID(ST_MakePoint(CAST(:longitude AS double precision), CAST(:latitude AS double precision)), 4326)::geography, CAST(:distance AS double precision))) " +
                    "AND p.is_available = true",

            countQuery =
                    "SELECT COUNT(*) FROM products p " +
                            "WHERE (:categories IS NULL OR cardinality(:categories) = 0 OR CAST(p.category AS text[]) && CAST(:categories AS text[])) " +
                            "AND (:sizes IS NULL OR cardinality(:sizes) = 0 OR CAST(p.size AS text[]) && CAST(:sizes AS text[])) " +
                            "AND (:colors IS NULL OR cardinality(:colors) = 0 OR CAST(p.color AS text[]) && CAST(:colors AS text[])) " +
                            "AND (:styleTags IS NULL OR cardinality(:styleTags) = 0 OR CAST(p.style_tags AS text[]) && CAST(:styleTags AS text[])) " +
                            "AND (:condition IS NULL OR p.condition = :condition) " +
                            "AND (:minRentalPrice IS NULL OR p.rental_price >= :minRentalPrice) " +
                            "AND (:maxRentalPrice IS NULL OR p.rental_price <= :maxRentalPrice) " +
                            "AND (:minDeposit IS NULL OR p.security_deposit >= :minDeposit) " +
                            "AND (:maxDeposit IS NULL OR p.security_deposit <= :maxDeposit) " +
                            "AND (:availableFrom IS NULL OR p.available_till >= :availableFrom) " +
                            "AND (:availableTill IS NULL OR p.available_from <= :availableTill) " +
                            "AND ((:latitude IS NULL OR :longitude IS NULL OR :distance IS NULL) OR " +
                            "ST_DWithin(p.location, ST_SetSRID(ST_MakePoint(CAST(:longitude AS double precision), CAST(:latitude AS double precision)), 4326)::geography, CAST(:distance AS double precision))) " +
                            "AND p.is_available = true",

            nativeQuery = true)
    Page<Product> findProductsWithFilters(
            @Param("categories") String[] categories,
            @Param("sizes") String[] sizes,
            @Param("colors") String[] colors,
            @Param("styleTags") String[] styleTags,
            @Param("condition") String condition,
            @Param("minRentalPrice") Double minRentalPrice,
            @Param("maxRentalPrice") Double maxRentalPrice,
            @Param("minDeposit") Double minDeposit,
            @Param("maxDeposit") Double maxDeposit,
            @Param("availableFrom") LocalDate availableFrom,
            @Param("availableTill") LocalDate availableTill,
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("distance") Double distance,
            Pageable pageable);*/

}