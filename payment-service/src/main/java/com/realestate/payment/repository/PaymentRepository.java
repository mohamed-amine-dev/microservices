package com.realestate.payment.repository;

import com.realestate.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByRentalId(Long rentalId);

    List<Payment> findByPayerId(Long payerId);

    List<Payment> findByPayeeId(Long payeeId);
}
