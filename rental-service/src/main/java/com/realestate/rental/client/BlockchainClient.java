package com.realestate.rental.client;

import com.realestate.common.dto.ResponseWrapper;
import com.realestate.common.dto.SmartContractRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "blockchain-integration-service")
public interface BlockchainClient {

    @PostMapping("/api/blockchain/transaction")
    ResponseWrapper<Map<String, Object>> executeTransaction(@RequestBody SmartContractRequest request);

    @GetMapping("/api/blockchain/contract/{address}")
    ResponseWrapper<Map<String, Object>> getContractState(@PathVariable("address") String address);
}
