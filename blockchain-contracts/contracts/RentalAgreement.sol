// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

contract RentalAgreement {
    struct Agreement {
        uint256 id;
        address tenant;
        address owner;
        uint256 rentAmount;
        uint256 deposit;
        bool isActive;
    }

    mapping(uint256 => Agreement) public agreements;
    uint256 public nextAgreementId;

    event AgreementCreated(uint256 indexed agreementId, address tenant, address owner);
    event PaymentReceived(uint256 indexed agreementId, address indexed from, uint256 amount);

    function createAgreement(address _owner, uint256 _rentAmount, uint256 _deposit) external returns (uint256) {
        uint256 agreementId = nextAgreementId++;
        agreements[agreementId] = Agreement({
            id: agreementId,
            tenant: msg.sender,
            owner: _owner,
            rentAmount: _rentAmount,
            deposit: _deposit,
            isActive: true
        });

        emit AgreementCreated(agreementId, msg.sender, _owner);
        return agreementId;
    }

    function payRent(uint256 _agreementId) external payable {
        Agreement storage agreement = agreements[_agreementId];
        require(agreement.isActive, "Agreement is not active");
        require(msg.value >= agreement.rentAmount, "Insufficient rent amount");
        
        payable(agreement.owner).transfer(msg.value);
        emit PaymentReceived(_agreementId, msg.sender, msg.value);
    }

    function terminateAgreement(uint256 _agreementId) external {
        Agreement storage agreement = agreements[_agreementId];
        require(msg.sender == agreement.owner || msg.sender == agreement.tenant, "Unauthorized");
        agreement.isActive = false;
    }
}
