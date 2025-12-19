package com.realestate.blockchain.service;

import com.realestate.blockchain.model.RentalAgreement;
import com.realestate.common.dto.SmartContractRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class BlockchainService {

    private final Web3j web3j;
    private final Credentials credentials;

    @Value("${blockchain.contract-address}")
    private String contractAddress;

    @Autowired
    public BlockchainService(Web3j web3j, Credentials credentials) {
        this.web3j = web3j;
        this.credentials = credentials;
    }

    public Map<String, Object> executeTransaction(SmartContractRequest request) {
        log.info("Executing blockchain transaction: {}", request.getFunctionName());

        try {
            RentalAgreement contract = RentalAgreement.load(
                    contractAddress, web3j, credentials, new DefaultGasProvider());

            if ("createAgreement".equalsIgnoreCase(request.getFunctionName())) {
                String owner = String.valueOf(request.getParameters().get("owner"));
                BigInteger rentAmount = new BigInteger(String.valueOf(request.getParameters().get("rentAmount")));
                BigInteger deposit = new BigInteger(String.valueOf(request.getParameters().get("deposit")));

                return mapReceipt(contract.createAgreement(owner, rentAmount, deposit).send());

            } else if ("payRent".equalsIgnoreCase(request.getFunctionName())) {
                BigInteger agreementId = new BigInteger(String.valueOf(request.getParameters().get("agreementId")));
                BigInteger amount = new BigInteger(String.valueOf(request.getParameters().get("amount")));

                // In Solidity, payRent(uint256) is payable.
                // We send the 'amount' as msg.value (in Wei).
                return mapReceipt(contract.payRent(agreementId, amount).send());
            } else if ("terminateAgreement".equalsIgnoreCase(request.getFunctionName())) {
                BigInteger agreementId = new BigInteger(String.valueOf(request.getParameters().get("agreementId")));

                return mapReceipt(contract.terminateAgreement(agreementId).send());
            }

            throw new IllegalArgumentException("Unsupported blockchain function: " + request.getFunctionName());

        } catch (Exception e) {
            log.error("Blockchain transaction failed for function: {}", request.getFunctionName(), e);
            throw new RuntimeException("Blockchain execution error: " + e.getMessage(), e);
        }
    }

    public Map<String, Object> getContractState(String address) {
        String targetAddress = (address != null && !address.isEmpty()) ? address : contractAddress;
        log.info("Fetching contract state for: {}", targetAddress);

        try {
            RentalAgreement contract = RentalAgreement.load(
                    targetAddress, web3j, credentials, new DefaultGasProvider());

            Map<String, Object> state = new HashMap<>();
            state.put("address", targetAddress);
            state.put("nextAgreementId", contract.nextAgreementId().send());

            return state;
        } catch (Exception e) {
            log.error("Failed to fetch contract state for: {}", targetAddress, e);
            throw new RuntimeException("Blockchain state fetch error: " + e.getMessage(), e);
        }
    }

    private Map<String, Object> mapReceipt(TransactionReceipt receipt) {
        Map<String, Object> result = new HashMap<>();
        result.put("transactionHash", receipt.getTransactionHash());
        result.put("status", receipt.isStatusOK() ? "SUCCESS" : "FAILED");
        result.put("blockNumber", receipt.getBlockNumber());
        result.put("gasUsed", receipt.getGasUsed());

        // Include events if needed in the future
        return result;
    }
}
