package com.realestate.property.dto;

import com.realestate.property.entity.PropertyType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyRequest {
    
    @NotBlank(message = "Title is required")
    @Size(max = 255)
    private String title;
    
    private String description;
    
    @NotBlank(message = "Address is required")
    private String address;
    
    @NotBlank(message = "City is required")
    private String city;
    
    @NotBlank(message = "Country is required")
    private String country;
    
    private Double latitude;
    private Double longitude;
    
    @NotNull(message = "Price per month is required")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal pricePerMonth;
    
    @NotNull(message = "Deposit amount is required")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal depositAmount;
    
    @NotNull(message = "Bedroom count is required")
    @Min(0)
    private Integer bedroomCount;
    
    @NotNull(message = "Bathroom count is required")
    @Min(0)
    private Integer bathroomCount;
    
    @NotNull(message = "Square meters is required")
    @DecimalMin(value = "0.0", inclusive = false)
    private Double squareMeters;
    
    @NotNull(message = "Property type is required")
    private PropertyType propertyType;
    
    @NotNull(message = "Owner ID is required")
    private Long ownerId;
}
