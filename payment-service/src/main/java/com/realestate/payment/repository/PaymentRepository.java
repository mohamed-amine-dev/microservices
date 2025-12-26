package com.realestate.payment.repository;

import com.realestate.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByRentalId(Long rentalId);

    org.springframework.data.domain.Page<Payment> findByRentalId(Long rentalId,
            org.springframework.data.domain.Pageable pageable);

    List<Payment> findByPayerId(Long payerId);

    org.springframework.data.domain.Page<Payment> findByPayerId(Long payerId,
            org.springframework.data.domain.Pageable pageable);

    List<Payment> findByPayeeId(Long payeeId);

    org.springframework.data.domain.Page<Payment> findByPayeeId(Long payeeId,
            org.springframework.data.domain.Pageable pageable);
}
