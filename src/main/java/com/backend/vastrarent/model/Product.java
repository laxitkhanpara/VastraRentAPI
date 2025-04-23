package com.backend.vastrarent.model;

import com.backend.vastrarent.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import jakarta.persistence.Column;
import org.hibernate.annotations.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private List<String> category=new ArrayList<>();

    @Column(nullable = false)
    private List<String> size = new ArrayList<>();

    @Column(nullable = false)
    private List<String> Color = new ArrayList<>();

    @Column(nullable = false)
    private List<String> styleTags = new ArrayList<>();

    private String condition;

    @Column(nullable = false)
    private double Retail;

    @Column(name = "rental_price",nullable = false)
    private double  rentalPrice;

    @Column(nullable = false)
    private double  securityDeposit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    private String careInstructions;
    //Address
    private String country;
    private String state;
    private String city;
    private String postalCode;
    private String address;
    private Double  latitude;
    private Double  longitude;

    private boolean isAvailable = true;
    private boolean termAndCondition;

    private int views = 0;

    private int quantity = 1;

    @Column(nullable = false)
    private LocalDate availableFrom;
    @Column(nullable = false)
    private LocalDate availableTill;

    @ElementCollection
    private List<String> imageUrls = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    @Column(columnDefinition = "geography(Point,4326)")
    private Point location;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    public void setLocationFromLatLng() {
        if (latitude != null && longitude != null) {
            this.location = new GeometryFactory().createPoint(new Coordinate(longitude, latitude));
        }
    }
}
