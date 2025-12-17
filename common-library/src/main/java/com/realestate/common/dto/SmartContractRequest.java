package com.realestate.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmartContractRequest {
    @NotBlank(message = "Function name is required")
    private String functionName;

    @NotNull(message = "Property ID is required")
    private Long propertyId;

    private String walletAddress;
    private BigDecimal value;
    private String contractAddress;
}
