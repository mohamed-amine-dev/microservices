package com.realestate.blockchain.controller;

import com.realestate.common.dto.ResponseWrapper;
import com.realestate.common.dto.SmartContractRequest;
import com.realestate.blockchain.service.BlockchainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/blockchain")
@RequiredArgsConstructor
@Tag(name = "Blockchain Integration")
public class BlockchainController {

    private final BlockchainService blockchainService;

    @PostMapping("/transaction")
    @Operation(summary = "Execute smart contract transaction")
    public ResponseEntity<ResponseWrapper<Map<String, Object>>> executeTransaction(
            @Valid @RequestBody SmartContractRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseWrapper.success("Transaction submitted", blockchainService.executeTransaction(request)));
    }

    @GetMapping("/contract/{address}")
    @Operation(summary = "Get smart contract state")
    public ResponseEntity<ResponseWrapper<Map<String, Object>>> getContractState(
            @PathVariable("address") String address) {
        return ResponseEntity.ok(ResponseWrapper.success(blockchainService.getContractState(address)));
    }
}
