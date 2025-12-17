package com.realestate.rental.repository;

import com.realestate.rental.entity.RentalAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<RentalAgreement, Long> {
    List<RentalAgreement> findByTenantId(Long tenantId);

    List<RentalAgreement> findByOwnerId(Long ownerId);

    List<RentalAgreement> findByPropertyId(Long propertyId);
}
