package com.realestate.property.dto;

import com.realestate.property.entity.PropertyStatus;
import com.realestate.property.entity.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyResponse {
    
    private Long id;
    private String title;
    private String description;
    private String address;
    private String city;
    private String country;
    private Double latitude;
    private Double longitude;
    private BigDecimal pricePerMonth;
    private BigDecimal depositAmount;
    private Integer bedroomCount;
    private Integer bathroomCount;
    private Double squareMeters;
    private PropertyType propertyType;
    private PropertyStatus status;
    private Long ownerId;
    private List<String> imageUrls;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
