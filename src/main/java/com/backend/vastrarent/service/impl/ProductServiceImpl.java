package com.backend.vastrarent.service.impl;

import com.backend.vastrarent.dto.ProductDto.*;
import com.backend.vastrarent.exception.ResourceNotFoundException;
import com.backend.vastrarent.mapper.ProductMapper;
import com.backend.vastrarent.model.Product;
import com.backend.vastrarent.model.User;
import com.backend.vastrarent.model.enums.Status;
import com.backend.vastrarent.repository.ProductRepository;
import com.backend.vastrarent.repository.UserRepository;
import com.backend.vastrarent.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductMapper productMapper;

    @Transactional
    @Override
    public ProductDTO createProduct(ProductRequest productRequest, Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        log.info("********************************{}",owner);
        Product product = productMapper.toEntity(productRequest, owner);
        log.info("********************************{}",product);
        product.setLocationFromLatLng();
        log.info("********************************{}",product);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }

    @Transactional(readOnly = true)
    @Override
    public ProductUserDTO getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        // Increment view count
        increaseViewCount(product);

        return productMapper.toProductUserDto(product);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductUserDTO> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(productMapper::toProductUserDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductDTO> getAvailableProducts(Pageable pageable) {
        Page<Product> products = productRepository.findByIsAvailableTrue(pageable);
        return products.map(productMapper::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductDTO> getProductsByOwnerId(Long ownerId) {
        List<Product> products = productRepository.findByOwner_Id(ownerId);
        return products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ProductDTO updateProduct(Long productId, ProductRequest productRequest, Long userId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        // Check if the user is the owner of the product
        if (!product.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("You are not authorized to update this product");
        }
        product.setLocationFromLatLng();
        productMapper.updateProductFromRequest(product, productRequest);
        Product updatedProduct = productRepository.save(product);

        return productMapper.toDto(updatedProduct);
    }

    @Transactional
    @Override
    public void deleteProduct(Long productId, Long userId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        // Check if the user is the owner of the product
        if (!product.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("You are not authorized to delete this product");
        }

        productRepository.delete(product);
    }

    @Transactional
    @Override
    public ProductDTO updateProductStatus(Long productId, Status status, Long userId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        // Check if the user is the owner of the product
        if (!product.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("You are not authorized to update this product");
        }

        product.setStatus(status);
        Product updatedProduct = productRepository.save(product);

        return productMapper.toDto(updatedProduct);
    }

    @Transactional
    @Override
    public ProductDTO toggleProductAvailability(Long productId, Long userId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        // Check if the user is the owner of the product
        if (!product.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("You are not authorized to update this product");
        }

        product.setAvailable(!product.isAvailable());
        Product updatedProduct = productRepository.save(product);

        return productMapper.toDto(updatedProduct);
    }
/*

    @Transactional(readOnly = true)
    @Override
    public Page<ProductDTO> searchProducts(
            String category,
            String city,
            Double minPrice,
            Double maxPrice,
            LocalDate availableFrom,
            LocalDate availableTill,
            Pageable pageable) {

        Page<Product> products = productRepository.searchProducts(
                category, city, minPrice, maxPrice, availableFrom, availableTill, pageable);

        return products.map(productMapper::toDto);
    }
*/

    @Transactional(readOnly = true)
    @Override
    public List<ProductDTO> getTopViewedProducts() {
        List<Product> products = productRepository.findTop10ByOrderByViewsDesc();
        return products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductDTO> searchProductsByKeyword(String keyword) {
        List<Product> products = productRepository.findByTitleContainingIgnoreCase(keyword);
        return products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void increaseViewCount(Product product) {
        product.setViews(product.getViews() + 1);
        productRepository.save(product);
    }


    // filter by near product
    @Transactional
    @Override
    public Page<ProductUserDTO> findNearbyProducts(double latitude, double longitude,
                                                   double distanceInMeters, int page, int size){
        Pageable pageable = PageRequest.of(page, size);

        Page<Product> product = productRepository.findNearbyProducts( latitude, longitude, distanceInMeters, pageable);
        return product.map(productMapper::toProductUserDto);
    }


/*
    @Transactional
    @Override
    public Page<ProductDTO> searchProducts(ProductSearchRequest request) {
        // Parse date strings to LocalDate objects if provided
        LocalDate fromDate = request.getAvailableFrom() != null ?
                LocalDate.parse(request.getAvailableFrom()) : null;
        LocalDate tillDate = request.getAvailableTill() != null ?
                LocalDate.parse(request.getAvailableTill()) : null;

        // Set up pagination and sorting
        Sort sort;
        if (request.getSortBy() != null) {
            sort = request.getSortDirection() != null && request.getSortDirection().equalsIgnoreCase("ASC") ?
                    Sort.by(request.getSortBy()).ascending() :
                    Sort.by(request.getSortBy()).descending();
        } else {
            sort = Sort.by("createdAt").descending();
        }

        int pageNumber = request.getPage() != null ? request.getPage() : 0;
        int pageSize = request.getSize() != null ? request.getSize() : 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        // Execute the search with all filters
        Page<Product> products = productRepository.findProductsWithFilters(
                request.getCategories(),
                request.getSizes(),
                request.getColors(),
                request.getStyleTags(),
                request.getCondition(),
                request.getMinRentalPrice(),
                request.getMaxRentalPrice(),
                request.getMinDeposit(),
                request.getMaxDeposit(),
                fromDate,
                tillDate,
                request.getLatitude(),
                request.getLongitude(),
                request.getDistance(),
                pageable
        );

        // Convert to DTOs
        return products.map(productMapper::toDto);
    }
*/


}



