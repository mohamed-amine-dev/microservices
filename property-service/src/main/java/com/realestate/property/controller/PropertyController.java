package com.realestate.property.controller;

import com.realestate.common.dto.ResponseWrapper;
import com.realestate.property.dto.PropertyRequest;
import com.realestate.property.dto.PropertyResponse;
import com.realestate.property.entity.PropertyStatus;
import com.realestate.property.service.PropertyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
@Tag(name = "Property Management")
public class PropertyController {

    private final PropertyService propertyService;

    @PostMapping
    @Operation(summary = "Create property")
    public ResponseEntity<ResponseWrapper<PropertyResponse>> createProperty(
            @Valid @RequestBody PropertyRequest request) {
        PropertyResponse response = propertyService.createProperty(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.success("Property created", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<PropertyResponse>> getProperty(@PathVariable Long id) {
        return ResponseEntity.ok(ResponseWrapper.success(propertyService.getPropertyById(id)));
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseWrapper<org.springframework.data.domain.Page<PropertyResponse>>> searchProperties(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer bedrooms,
            @RequestParam(required = false) PropertyStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        org.springframework.data.domain.Page<PropertyResponse> properties = propertyService.searchProperties(
                city, country, minPrice, maxPrice, bedrooms, status, pageable);
        return ResponseEntity.ok(ResponseWrapper.success(properties));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<PropertyResponse>> updateProperty(
            @PathVariable Long id, @Valid @RequestBody PropertyRequest request) {
        return ResponseEntity
                .ok(ResponseWrapper.success("Property updated", propertyService.updateProperty(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Void>> deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.ok(ResponseWrapper.success("Property deleted"));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ResponseWrapper<PropertyResponse>> updateStatus(
            @PathVariable Long id, @RequestParam PropertyStatus status) {
        return ResponseEntity.ok(ResponseWrapper.success(propertyService.updateStatus(id, status)));
    }
}
