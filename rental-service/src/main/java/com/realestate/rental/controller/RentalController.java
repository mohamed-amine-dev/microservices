package com.realestate.rental.controller;

import com.realestate.common.dto.ResponseWrapper;
import com.realestate.rental.dto.RentalRequest;
import com.realestate.rental.dto.RentalResponse;
import com.realestate.rental.entity.RentalStatus;
import com.realestate.rental.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
@Tag(name = "Rental Management", description = "APIs for rental agreements")
public class RentalController {

    private final RentalService rentalService;

    @PostMapping
    @Operation(summary = "Create rental agreement")
    public ResponseEntity<ResponseWrapper<RentalResponse>> createRental(@Valid @RequestBody RentalRequest request) {
        RentalResponse response = rentalService.createRental(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseWrapper.success("Rental agreement created successfully", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get rental by ID")
    public ResponseEntity<ResponseWrapper<RentalResponse>> getRental(@PathVariable Long id) {
        return ResponseEntity.ok(ResponseWrapper.success(rentalService.getRentalById(id)));
    }

    @GetMapping("/tenant/{tenantId}")
    @Operation(summary = "Get rentals by tenant")
    public ResponseEntity<ResponseWrapper<List<RentalResponse>>> getRentalsByTenant(@PathVariable Long tenantId) {
        return ResponseEntity.ok(ResponseWrapper.success(rentalService.getRentalsByTenant(tenantId)));
    }

    @GetMapping("/owner/{ownerId}")
    @Operation(summary = "Get rentals by owner")
    public ResponseEntity<ResponseWrapper<List<RentalResponse>>> getRentalsByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(ResponseWrapper.success(rentalService.getRentalsByOwner(ownerId)));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update rental status")
    public ResponseEntity<ResponseWrapper<RentalResponse>> updateStatus(
            @PathVariable Long id,
            @RequestParam RentalStatus status) {
        return ResponseEntity.ok(ResponseWrapper.success("Status updated", rentalService.updateStatus(id, status)));
    }
}
