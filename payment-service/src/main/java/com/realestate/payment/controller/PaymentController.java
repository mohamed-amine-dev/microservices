package com.realestate.payment.controller;

import com.realestate.common.dto.ResponseWrapper;
import com.realestate.payment.dto.PaymentRequest;
import com.realestate.payment.entity.Payment;
import com.realestate.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payment Management")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @Operation(summary = "Process a payment")
    public ResponseEntity<ResponseWrapper<Payment>> processPayment(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity
                .ok(ResponseWrapper.success("Payment processed successfully", paymentService.processPayment(request)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get payment by ID")
    public ResponseEntity<ResponseWrapper<Payment>> getPayment(@PathVariable Long id) {
        return ResponseEntity.ok(ResponseWrapper.success(paymentService.getPaymentById(id)));
    }

    @GetMapping("/rental/{rentalId}")
    @Operation(summary = "Get payments for a rental")
    public ResponseEntity<ResponseWrapper<List<Payment>>> getPaymentsByRental(@PathVariable Long rentalId) {
        return ResponseEntity.ok(ResponseWrapper.success(paymentService.getPaymentsByRental(rentalId)));
    }

    @GetMapping("/payer/{payerId}")
    @Operation(summary = "Get payments by payer")
    public ResponseEntity<ResponseWrapper<List<Payment>>> getPaymentsByPayer(@PathVariable Long payerId) {
        return ResponseEntity.ok(ResponseWrapper.success(paymentService.getPaymentsByPayer(payerId)));
    }
}
