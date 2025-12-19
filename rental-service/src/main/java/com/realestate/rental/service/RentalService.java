package com.realestate.rental.service;

import com.realestate.common.dto.NotificationEvent;
import com.realestate.common.dto.Role;
import com.realestate.common.dto.UserResponse;
import com.realestate.rental.dto.RentalRequest;
import com.realestate.rental.dto.RentalResponse;
import com.realestate.rental.entity.RentalAgreement;
import com.realestate.rental.entity.RentalStatus;
import com.realestate.rental.producer.RentalEventProducer;
import com.realestate.rental.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentalService {

    private final RentalRepository rentalRepository;
    private final RentalEventProducer rentalEventProducer;
    private final com.realestate.rental.client.BlockchainClient blockchainClient;
    private final com.realestate.rental.client.UserClient userClient;

    @Transactional
    public RentalResponse createRental(RentalRequest request) {
        log.info("Creating rental agreement for property: {}", request.getPropertyId());

        // Fetch user data for extra information
        com.realestate.common.dto.UserResponse tenant = null;
        com.realestate.common.dto.UserResponse owner = null;
        try {
            var tenantResp = userClient.getUserById(request.getTenantId());
            if (tenantResp != null)
                tenant = tenantResp.getData();

            var ownerResp = userClient.getUserById(request.getOwnerId());
            if (ownerResp != null)
                owner = ownerResp.getData();
        } catch (Exception e) {
            log.warn("Could not fetch user data for rental creation: {}", e.getMessage());
        }

        RentalAgreement agreement = RentalAgreement.builder()
                .propertyId(request.getPropertyId())
                .tenantId(request.getTenantId())
                .ownerId(request.getOwnerId())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .monthlyRent(request.getMonthlyRent())
                .depositAmount(request.getDepositAmount())
                .status(RentalStatus.PENDING)
                .build();

        RentalAgreement savedAgreement = rentalRepository.save(agreement);

        // Record agreement on blockchain
        try {
            com.realestate.common.dto.SmartContractRequest blockchainRequest = new com.realestate.common.dto.SmartContractRequest();
            blockchainRequest.setFunctionName("createAgreement");

            // Use wallet address if available, otherwise fallback to ID
            String ownerAddress = (owner != null && owner.getWalletAddress() != null)
                    ? owner.getWalletAddress()
                    : "0x" + agreement.getOwnerId();

            blockchainRequest.setParameters(java.util.Map.of(
                    "owner", ownerAddress,
                    "rentAmount", agreement.getMonthlyRent(),
                    "deposit", agreement.getDepositAmount()));

            var response = blockchainClient.executeTransaction(blockchainRequest);
            if (response != null && response.getData() != null) {
                log.info("Blockchain transaction successful. Hash: {}", response.getData().get("transactionHash"));
            }
        } catch (Exception e) {
            log.error("Failed to record agreement on blockchain", e);
        }

        // Send notification event
        try {
            String tenantEmail = (tenant != null) ? tenant.getEmail() : "tenant@example.com";

            NotificationEvent event = NotificationEvent.builder()
                    .recipientId(savedAgreement.getTenantId())
                    .recipientEmail(tenantEmail)
                    .subject("Rental Agreement Created")
                    .message("Your rental agreement for property " + savedAgreement.getPropertyId()
                            + " has been created.")
                    .type("RENTAL_CREATED")
                    .build();
            rentalEventProducer.sendNotificationEvent(event);
        } catch (Exception e) {
            log.error("Failed to send notification event", e);
        }

        return mapToResponse(savedAgreement);
    }

    public RentalResponse getRentalById(Long id) {
        RentalAgreement agreement = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental agreement not found with id: " + id));
        return mapToResponse(agreement);
    }

    public List<RentalResponse> getRentalsByTenant(Long tenantId) {
        return rentalRepository.findByTenantId(tenantId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<RentalResponse> getRentalsByOwner(Long ownerId) {
        return rentalRepository.findByOwnerId(ownerId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public RentalResponse updateStatus(Long id, RentalStatus status) {
        RentalAgreement agreement = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental agreement not found with id: " + id));

        agreement.setStatus(status);
        RentalAgreement updated = rentalRepository.save(agreement);
        return mapToResponse(updated);
    }

    private RentalResponse mapToResponse(RentalAgreement agreement) {
        return RentalResponse.builder()
                .id(agreement.getId())
                .propertyId(agreement.getPropertyId())
                .tenantId(agreement.getTenantId())
                .ownerId(agreement.getOwnerId())
                .startDate(agreement.getStartDate())
                .endDate(agreement.getEndDate())
                .monthlyRent(agreement.getMonthlyRent())
                .depositAmount(agreement.getDepositAmount())
                .status(agreement.getStatus())
                .smartContractAddress(agreement.getSmartContractAddress())
                .createdAt(agreement.getCreatedAt())
                .updatedAt(agreement.getUpdatedAt())
                .build();
    }
}
