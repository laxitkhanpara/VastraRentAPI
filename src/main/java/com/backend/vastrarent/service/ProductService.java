package com.backend.vastrarent.service;
import com.backend.vastrarent.dto.ProductDto.*;
import com.backend.vastrarent.exception.ResourceNotFoundException;
import com.backend.vastrarent.mapper.ProductMapper;
import com.backend.vastrarent.model.Product;
import com.backend.vastrarent.model.User;
import com.backend.vastrarent.model.enums.Status;
import com.backend.vastrarent.repository.ProductRepository;
import com.backend.vastrarent.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


public interface ProductService {

    @Transactional
    ProductDTO createProduct(ProductRequest productRequest, Long userId);

    @Transactional(readOnly = true)
    ProductDTO getProductById(Long productId);

    @Transactional(readOnly = true)
    Page<ProductDTO> getAllProducts(Pageable pageable);

    @Transactional(readOnly = true)
    Page<ProductDTO> getAvailableProducts(Pageable pageable);

    @Transactional(readOnly = true)
    List<ProductDTO> getProductsByOwnerId(Long ownerId);

    @Transactional
    ProductDTO updateProduct(Long productId, ProductRequest productRequest, Long userId);

    @Transactional
    void deleteProduct(Long productId, Long userId);

    @Transactional
    ProductDTO updateProductStatus(Long productId, Status status, Long userId);

    @Transactional
    ProductDTO toggleProductAvailability(Long productId, Long userId);

    @Transactional(readOnly = true)
    Page<ProductDTO> searchProducts(
            String category,
            String city,
            Double minPrice,
            Double maxPrice,
            LocalDate availableFrom,
            LocalDate availableTill,
            Pageable pageable);

    @Transactional(readOnly = true)
    List<ProductDTO> getTopViewedProducts();

    @Transactional(readOnly = true)
    List<ProductDTO> searchProductsByKeyword(String keyword);

    @Transactional
    void increaseViewCount(Product product);
}