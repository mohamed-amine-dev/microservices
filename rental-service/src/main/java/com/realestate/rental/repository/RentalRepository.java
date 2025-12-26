package com.realestate.rental.repository;

import com.realestate.rental.entity.RentalAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<RentalAgreement, Long> {
    List<RentalAgreement> findByTenantId(Long tenantId);

    org.springframework.data.domain.Page<RentalAgreement> findByTenantId(Long tenantId,
            org.springframework.data.domain.Pageable pageable);

    List<RentalAgreement> findByOwnerId(Long ownerId);

    org.springframework.data.domain.Page<RentalAgreement> findByOwnerId(Long ownerId,
            org.springframework.data.domain.Pageable pageable);

    List<RentalAgreement> findByPropertyId(Long propertyId);

    org.springframework.data.domain.Page<RentalAgreement> findByPropertyId(Long propertyId,
            org.springframework.data.domain.Pageable pageable);
}
