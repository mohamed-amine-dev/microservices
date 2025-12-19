package com.realestate.blockchain.model;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

/**
 * Simplified Java wrapper for the RentalAgreement Solidity contract.
 */
public class RentalAgreement extends Contract {

    protected RentalAgreement(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider gasProvider) {
        super("", contractAddress, web3j, credentials, gasProvider);
    }

    protected RentalAgreement(String contractAddress, Web3j web3j, TransactionManager transactionManager,
            ContractGasProvider gasProvider) {
        super("", contractAddress, web3j, transactionManager, gasProvider);
    }

    public static RentalAgreement load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider gasProvider) {
        return new RentalAgreement(contractAddress, web3j, credentials, gasProvider);
    }

    public RemoteCall<TransactionReceipt> createAgreement(String owner, BigInteger rentAmount, BigInteger deposit) {
        final Function function = new Function(
                "createAgreement",
                Arrays.asList(new Address(owner), new Uint256(rentAmount), new Uint256(deposit)),
                Collections.singletonList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> payRent(BigInteger agreementId, BigInteger weiValue) {
        final Function function = new Function(
                "payRent",
                Collections.singletonList(new Uint256(agreementId)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<TransactionReceipt> terminateAgreement(BigInteger agreementId) {
        final Function function = new Function(
                "terminateAgreement",
                Collections.singletonList(new Uint256(agreementId)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> nextAgreementId() {
        final Function function = new Function(
                "nextAgreementId",
                Collections.emptyList(),
                Collections.singletonList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Agreement> agreements(BigInteger index) {
        final Function function = new Function(
                "agreements",
                Collections.singletonList(new Uint256(index)),
                Arrays.asList(
                        new TypeReference<Uint256>() {
                        },
                        new TypeReference<Address>() {
                        },
                        new TypeReference<Address>() {
                        },
                        new TypeReference<Uint256>() {
                        },
                        new TypeReference<Uint256>() {
                        },
                        new TypeReference<Bool>() {
                        }));
        return new RemoteCall<>(() -> {
            java.util.List<Type> results = executeCallMultipleValueReturn(function);
            return new Agreement(
                    (BigInteger) results.get(0).getValue(),
                    (String) results.get(1).getValue(),
                    (String) results.get(2).getValue(),
                    (BigInteger) results.get(3).getValue(),
                    (BigInteger) results.get(4).getValue(),
                    (Boolean) results.get(5).getValue());
        });
    }

    public static class Agreement {
        public BigInteger id;
        public String tenant;
        public String owner;
        public BigInteger rentAmount;
        public BigInteger deposit;
        public Boolean isActive;

        public Agreement(BigInteger id, String tenant, String owner, BigInteger rentAmount, BigInteger deposit,
                Boolean isActive) {
            this.id = id;
            this.tenant = tenant;
            this.owner = owner;
            this.rentAmount = rentAmount;
            this.deposit = deposit;
            this.isActive = isActive;
        }
    }
}
