package com.realestate.property.service;

import com.realestate.common.exception.ResourceNotFoundException;
import com.realestate.property.dto.PropertyRequest;
import com.realestate.property.dto.PropertyResponse;
import com.realestate.property.entity.Property;
import com.realestate.property.entity.PropertyStatus;
import com.realestate.property.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PropertyService {
    
    private final PropertyRepository propertyRepository;
    
    public PropertyResponse createProperty(PropertyRequest request) {
        Property property = Property.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .address(request.getAddress())
                .city(request.getCity())
                .country(request.getCountry())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .pricePerMonth(request.getPricePerMonth())
                .depositAmount(request.getDepositAmount())
                .bedroomCount(request.getBedroomCount())
                .bathroomCount(request.getBathroomCount())
                .squareMeters(request.getSquareMeters())
                .propertyType(request.getPropertyType())
                .ownerId(request.getOwnerId())
                .build();
        
        Property saved = propertyRepository.save(property);
        return toResponse(saved);
    }
    
    @Transactional(readOnly = true)
    public PropertyResponse getPropertyById(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property", "id", id));
        return toResponse(property);
    }
    
    @Transactional(readOnly = true)
    public List<PropertyResponse> searchProperties(String city, String country, 
            BigDecimal minPrice, BigDecimal maxPrice, Integer bedrooms, PropertyStatus status) {
        return propertyRepository.searchProperties(city, country, minPrice, maxPrice, bedrooms, status)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }
    
    public PropertyResponse updateProperty(Long id, PropertyRequest request) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property", "id", id));
        
        property.setTitle(request.getTitle());
        property.setDescription(request.getDescription());
        property.setAddress(request.getAddress());
        property.setCity(request.getCity());
        property.setCountry(request.getCountry());
        property.setPricePerMonth(request.getPricePerMonth());
        property.setDepositAmount(request.getDepositAmount());
        property.setBedroomCount(request.getBedroomCount());
        property.setBathroomCount(request.getBathroomCount());
        property.setSquareMeters(request.getSquareMeters());
        
        return toResponse(propertyRepository.save(property));
    }
    
    public void deleteProperty(Long id) {
        if (!propertyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Property", "id", id);
        }
        propertyRepository.deleteById(id);
    }
    
    public PropertyResponse updateStatus(Long id, PropertyStatus status) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property", "id", id));
        property.setStatus(status);
        return toResponse(propertyRepository.save(property));
    }
    
    private PropertyResponse toResponse(Property property) {
        return PropertyResponse.builder()
                .id(property.getId())
                .title(property.getTitle())
                .description(property.getDescription())
                .address(property.getAddress())
                .city(property.getCity())
                .country(property.getCountry())
                .latitude(property.getLatitude())
                .longitude(property.getLongitude())
                .pricePerMonth(property.getPricePerMonth())
                .depositAmount(property.getDepositAmount())
                .bedroomCount(property.getBedroomCount())
                .bathroomCount(property.getBathroomCount())
                .squareMeters(property.getSquareMeters())
                .propertyType(property.getPropertyType())
                .status(property.getStatus())
                .ownerId(property.getOwnerId())
                .imageUrls(property.getImages().stream().map(img -> img.getImageUrl()).collect(Collectors.toList()))
                .createdAt(property.getCreatedAt())
                .updatedAt(property.getUpdatedAt())
                .build();
    }
}
