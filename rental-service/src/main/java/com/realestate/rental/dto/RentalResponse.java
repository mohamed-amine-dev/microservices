package com.realestate.rental.dto;

import com.realestate.rental.entity.RentalStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class RentalResponse {
    private Long id;
    private Long propertyId;
    private Long tenantId;
    private Long ownerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal monthlyRent;
    private BigDecimal depositAmount;
    private RentalStatus status;
    private String smartContractAddress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
