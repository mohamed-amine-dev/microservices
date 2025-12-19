const fs = require('fs');
const path = require('path');

const artifactPath = 'blockchain-contracts/artifacts/contracts/RentalAgreement.sol/RentalAgreement.json';
const outputDir = 'blockchain-integration-service/src/main/resources/contracts';

if (fs.existsSync(artifactPath)) {
    const artifact = JSON.parse(fs.readFileSync(artifactPath, 'utf8'));
    fs.writeFileSync(path.join(outputDir, 'RentalAgreement.abi'), JSON.stringify(artifact.abi));
    fs.writeFileSync(path.join(outputDir, 'RentalAgreement.bin'), artifact.bytecode);
    console.log('ABI and BIN extracted successfully');
} else {
    console.error('Artifact not found: ' + artifactPath);
}
