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
                .functionName("payRent")
                .parameters(java.util.Map.of(
                        "agreementId", request.getRentalId().toString(), // Using rental ID as proxy
                        "amount", request.getAmount().toBigInteger().multiply(java.math.BigInteger.valueOf(10).pow(18)) // Simplistic
                                                                                                                        // ETH
                                                                                                                        // conversion
                ))
                .build();

        String txHash = "PENDING";
        try {
            var response = blockchainClient.executeTransaction(blockchainRequest);
            if (response != null && response.getData() != null) {
                txHash = (String) response.getData().get("transactionHash");
            }
        } catch (Exception e) {
            log.error("Blockchain transaction failed", e);
            Payment payment = Payment.builder()
                    .rentalId(request.getRentalId())
                    .payerId(request.getPayerId())
                    .payeeId(request.getPayeeId())
                    .amount(request.getAmount())
                    .currency(request.getCurrency())
                    .status(PaymentStatus.FAILED)
                    .transactionHash("FAILED")
                    .build();
            return paymentRepository.save(payment);
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

    public org.springframework.data.domain.Page<Payment> getPaymentsByRental(Long rentalId,
            org.springframework.data.domain.Pageable pageable) {
        return paymentRepository.findByRentalId(rentalId, pageable);
    }

    public org.springframework.data.domain.Page<Payment> getPaymentsByPayer(Long payerId,
            org.springframework.data.domain.Pageable pageable) {
        return paymentRepository.findByPayerId(payerId, pageable);
    }
}
