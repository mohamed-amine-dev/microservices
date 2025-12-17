package com.realestate.payment.client;

import com.realestate.common.dto.ResponseWrapper;
import com.realestate.common.dto.SmartContractRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "blockchain-integration-service", path = "/api/blockchain")
public interface BlockchainClient {

    @PostMapping("/transaction")
    ResponseWrapper<Map<String, Object>> executeTransaction(@RequestBody SmartContractRequest request);
}
