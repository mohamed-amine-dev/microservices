package com.realestate.payment.service;

import com.realestate.payment.client.BlockchainClient;
import com.realestate.payment.dto.PaymentRequest;
import com.realestate.common.dto.SmartContractRequest;
import com.realestate.payment.entity.Payment;
import com.realestate.payment.entity.PaymentStatus;
import com.realestate.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BlockchainClient blockchainClient;

    @Transactional
    public Payment processPayment(PaymentRequest request) {
        log.info("Processing payment for rental: {}", request.getRentalId());

        // 1. Execute Blockchain Transaction
        SmartContractRequest blockchainRequest = SmartContractRequest.builder()
                .functionName("processPayment")
                .propertyId(request.getRentalId()) // Using rental ID as proxy for simplicity
                .value(request.getAmount())
                .build();

        String txHash = "PENDING";
        try {
            var response = blockchainClient.executeTransaction(blockchainRequest);
            if (response.getData() != null) {
                txHash = (String) response.getData().get("transactionHash");
            }
        } catch (Exception e) {
            log.error("Blockchain transaction failed", e);
            // In a real app, you might want to throw an exception or mark as FAILED
        }

        // 2. Save Record
        Payment payment = Payment.builder()
                .rentalId(request.getRentalId())
                .payerId(request.getPayerId())
                .payeeId(request.getPayeeId())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .status(PaymentStatus.COMPLETED)
                .transactionHash(txHash)
                .build();

        return paymentRepository.save(payment);
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
    }

    public List<Payment> getPaymentsByRental(Long rentalId) {
        return paymentRepository.findByRentalId(rentalId);
    }

    public List<Payment> getPaymentsByPayer(Long payerId) {
        return paymentRepository.findByPayerId(payerId);
    }
}
