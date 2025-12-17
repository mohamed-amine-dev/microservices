package com.realestate.rental.service;

import com.realestate.common.dto.NotificationEvent;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentalService {

    private final RentalRepository rentalRepository;
    private final RentalEventProducer rentalEventProducer;

    @Transactional
    public RentalResponse createRental(RentalRequest request) {
        log.info("Creating rental agreement for property: {}", request.getPropertyId());

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

        // Send notification event
        try {
            NotificationEvent event = NotificationEvent.builder()
                    .recipientId(savedAgreement.getTenantId())
                    .recipientEmail("tenant@example.com") // In real app, fetch user email
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
