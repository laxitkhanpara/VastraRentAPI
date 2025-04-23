package com.backend.vastrarent.controller;
import com.backend.vastrarent.dto.ProductDto.*;

import com.backend.vastrarent.model.UserPrincipal;
import com.backend.vastrarent.model.enums.Status;
import com.backend.vastrarent.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest productRequest,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = getUserIdFromUserDetails(userPrincipal);
        ProductDTO createdProduct = productService.createProduct(productRequest, userId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ProductResponse("success", "Product created successfully", createdProduct));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long productId) {
        ProductUserDTO product = productService.getProductById(productId);
        return ResponseEntity.ok(new ProductResponse("success", "Product fetched successfully", product));
    }

    @GetMapping
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<ProductUserDTO> products = productService.getAllProducts(pageable);

        return ResponseEntity.ok(new ProductResponse("success", "Products fetched successfully", products));
    }

    @GetMapping("/available")
    public ResponseEntity<ProductResponse> getAvailableProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<ProductDTO> products = productService.getAvailableProducts(pageable);

        return ResponseEntity.ok(new ProductResponse("success", "Available products fetched successfully", products));
    }

    @GetMapping("/my-products")
    public ResponseEntity<ProductResponse> getMyProducts(   @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = getUserIdFromUserDetails(userPrincipal);
        List<ProductDTO> products = productService.getProductsByOwnerId(userId);

        return ResponseEntity.ok(new ProductResponse("success", "Your products fetched successfully", products));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody ProductRequest productRequest,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = getUserIdFromUserDetails(userPrincipal);
        ProductDTO updatedProduct = productService.updateProduct(productId, productRequest, userId);

        return ResponseEntity.ok(new ProductResponse("success", "Product updated successfully", updatedProduct));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ProductResponse> deleteProduct(
            @PathVariable Long productId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = getUserIdFromUserDetails(userPrincipal);;
        productService.deleteProduct(productId, userId);

        return ResponseEntity.ok(new ProductResponse("success", "Product deleted successfully", null));
    }

    @PatchMapping("/{productId}/status")
    public ResponseEntity<ProductResponse> updateProductStatus(
            @PathVariable Long productId,
            @RequestParam Status status,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = getUserIdFromUserDetails(userPrincipal);
        ProductDTO updatedProduct = productService.updateProductStatus(productId, status, userId);

        return ResponseEntity.ok(new ProductResponse("success", "Product status updated successfully", updatedProduct));
    }

    @PatchMapping("/{productId}/toggle-availability")
    public ResponseEntity<ProductResponse> toggleProductAvailability(
            @PathVariable Long productId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = getUserIdFromUserDetails(userPrincipal);
        ProductDTO updatedProduct = productService.toggleProductAvailability(productId, userId);

        return ResponseEntity.ok(new ProductResponse("success", "Product availability toggled successfully", updatedProduct));
    }

    /*@GetMapping("/search")
    public ResponseEntity<ProductResponse> searchProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate availableFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate availableTill,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<ProductDTO> products = productService.searchProducts(
                category, city, minPrice, maxPrice, availableFrom, availableTill, pageable);

        return ResponseEntity.ok(new ProductResponse("success", "Search results fetched successfully", products));
    }
*/
    @GetMapping("/trending")
    public ResponseEntity<ProductResponse> getTrendingProducts() {
        List<ProductDTO> products = productService.getTopViewedProducts();
        return ResponseEntity.ok(new ProductResponse("success", "Trending products fetched successfully", products));
    }

    @GetMapping("/search-by-keyword")
    public ResponseEntity<ProductResponse> searchProductsByKeyword(@RequestParam String keyword) {
        List<ProductDTO> products = productService.searchProductsByKeyword(keyword);
        return ResponseEntity.ok(new ProductResponse("success", "Search results fetched successfully", products));
    }



    @GetMapping("/nearby")
    public ResponseEntity<ProductResponse> getNearbyProducts(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "5000") double distance,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<ProductUserDTO> products = productService.findNearbyProducts(
                latitude, longitude, distance, page, size);
        return ResponseEntity.ok(new ProductResponse("success","Search results fetched successfully",products));
    }

    private Long getUserIdFromUserDetails(UserPrincipal userPrincipal) {
        // This is a placeholder implementation
        // In a real application, you would extract the user ID from the UserDetails object
        // depending on how your authentication is set up
        return userPrincipal.getId();
    }

/*    @PostMapping("/search")
    public ResponseEntity<Page<ProductDTO>> searchProducts(@RequestBody ProductSearchRequest request) {
        Page<ProductDTO> products = productService.searchProducts(request);
        return ResponseEntity.ok(products);
    }*/

}