package com.backend.vastrarent.mapper;

import com.backend.vastrarent.dto.ProductDto.*;
import com.backend.vastrarent.model.Product;
import com.backend.vastrarent.model.User;
import com.backend.vastrarent.model.enums.Status;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProductMapper {

    public ProductDTO toDto(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .title(product.getTitle())
                .description(product.getDescription())
                .category(product.getCategory())
                .size(product.getSize())
                .color(product.getColor())
                .styleTags(product.getStyleTags())
                .condition(product.getCondition())
                .retail(product.getRetail())
                .rentalPrice(product.getRentalPrice())
                .securityDeposit(product.getSecurityDeposit())
                .status(product.getStatus())
                .careInstructions(product.getCareInstructions())
                .country(product.getCountry())
                .state(product.getState())
                .city(product.getCity())
                .postalCode(product.getPostalCode())
                .address(product.getAddress())
                .latitude(product.getLatitude())
                .longitude(product.getLongitude())
                .isAvailable(product.isAvailable())
                .termAndCondition(product.isTermAndCondition())
                .views(product.getViews())
                .availableFrom(product.getAvailableFrom())
                .availableTill(product.getAvailableTill())
                .imageUrls(product.getImageUrls())
                .ownerId(product.getOwner() != null ? product.getOwner().getId() : null)
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .quntity((product.getQuantity()))
                .build();
    }

    public Product toEntity(ProductRequest productRequest, User owner) {
        return Product.builder()
                .title(productRequest.getTitle())
                .description(productRequest.getDescription())
                .category(productRequest.getCategory())
                .size(productRequest.getSize())
                .Color(productRequest.getColor())
                .styleTags(productRequest.getStyleTags())
                .condition(productRequest.getCondition())
                .Retail(productRequest.getRetail())
                .rentalPrice(productRequest.getRentalPrice())
                .securityDeposit(productRequest.getSecurityDeposit())
                .status(Status.AVAILABLE)
                .careInstructions(productRequest.getCareInstructions())
                .country(productRequest.getCountry())
                .state(productRequest.getState())
                .city(productRequest.getCity())
                .postalCode(productRequest.getPostalCode())
                .address(productRequest.getAddress())
                .latitude(productRequest.getLatitude())
                .longitude(productRequest.getLongitude())
                .quantity(productRequest.getQuntity())
                .isAvailable(true)
                .termAndCondition(productRequest.isTermAndCondition())
                .views(0)
                .availableFrom(productRequest.getAvailableFrom())
                .availableTill(productRequest.getAvailableTill())
                .imageUrls(productRequest.getImageUrls())
                .owner(owner)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
    public ProductUserDTO toProductUserDto(Product product) {
        return ProductUserDTO.builder()
                .id(product.getId())
                .title(product.getTitle())
                .description(product.getDescription())
                .category(product.getCategory())
                .size(product.getSize())
                .color(product.getColor())
                .styleTags(product.getStyleTags())
                .condition(product.getCondition())
                .retail(product.getRetail())
                .rentalPrice(product.getRentalPrice())
                .securityDeposit(product.getSecurityDeposit())
                .status(product.getStatus())
                .careInstructions(product.getCareInstructions())
                .country(product.getCountry())
                .state(product.getState())
                .city(product.getCity())
                .postalCode(product.getPostalCode())
                .address(product.getAddress())
                .latitude(product.getLatitude())
                .longitude(product.getLongitude())
                .isAvailable(product.isAvailable())
                .termAndCondition(product.isTermAndCondition())
                .views(product.getViews())
                .availableFrom(product.getAvailableFrom())
                .availableTill(product.getAvailableTill())
                .imageUrls(product.getImageUrls())
                .ownerId(product.getOwner() != null ? product.getOwner().getId() : null)
                .ownerProfile(product.getOwner() != null ? product.getOwner().getProfilePicture():null)
                .ownerName(product.getOwner() != null ? product.getOwner().getFullName():null)
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .quntity((product.getQuantity()))
                .build();
    }
    public void updateProductFromRequest(Product product, ProductRequest productRequest) {
        product.setTitle(productRequest.getTitle());
        product.setDescription(productRequest.getDescription());
        product.setCategory(productRequest.getCategory());
        product.setSize(productRequest.getSize());
        product.setColor(productRequest.getColor());
        product.setStyleTags(productRequest.getStyleTags());
        product.setCondition(productRequest.getCondition());
        product.setRetail(productRequest.getRetail());
        product.setRentalPrice(productRequest.getRentalPrice());
        product.setSecurityDeposit(productRequest.getSecurityDeposit());
        product.setCareInstructions(productRequest.getCareInstructions());
        product.setCountry(productRequest.getCountry());
        product.setState(productRequest.getState());
        product.setCity(productRequest.getCity());
        product.setPostalCode(productRequest.getPostalCode());
        product.setAddress(productRequest.getAddress());
        product.setLatitude(productRequest.getLatitude());
        product.setLongitude(productRequest.getLongitude());
        product.setQuantity(productRequest.getQuntity());
        product.setTermAndCondition(productRequest.isTermAndCondition());
        product.setAvailableFrom(productRequest.getAvailableFrom());
        product.setAvailableTill(productRequest.getAvailableTill());

        if (productRequest.getImageUrls() != null) {
            product.setImageUrls(productRequest.getImageUrls());
        }

        product.setUpdatedAt(LocalDateTime.now());
    }
}