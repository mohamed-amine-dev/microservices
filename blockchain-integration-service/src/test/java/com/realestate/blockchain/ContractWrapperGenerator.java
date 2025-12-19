package com.realestate.blockchain;

import org.web3j.codegen.SolidityFunctionWrapperGenerator;
import java.io.File;

public class ContractWrapperGenerator {
    public static void main(String[] args) throws Exception {
        String abiFile = "blockchain-integration-service/src/main/resources/contracts/RentalAgreement.abi";
        String binFile = "blockchain-integration-service/src/main/resources/contracts/RentalAgreement.bin";
        String outputDir = "blockchain-integration-service/src/main/java";
        String packageName = "com.realestate.blockchain.model";

        SolidityFunctionWrapperGenerator.main(new String[] {
                "-a", abiFile,
                "-b", binFile,
                "-o", outputDir,
                "-p", packageName
        });
        System.out.println("Wrapper generated successfully");
    }
}
