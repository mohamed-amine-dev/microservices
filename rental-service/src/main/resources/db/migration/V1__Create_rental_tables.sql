-- Create rental_agreements table
CREATE TABLE IF NOT EXISTS rental_agreements (
    id BIGSERIAL PRIMARY KEY,
    property_id BIGINT NOT NULL,
    tenant_id BIGINT NOT NULL,
    owner_id BIGINT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    monthly_rent DECIMAL(10, 2) NOT NULL,
    deposit_amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    smart_contract_address VARCHAR(255),
    transaction_hash VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_rental_agreements_property_id ON rental_agreements(property_id);
CREATE INDEX idx_rental_agreements_tenant_id ON rental_agreements(tenant_id);
CREATE INDEX idx_rental_agreements_owner_id ON rental_agreements(owner_id);
CREATE INDEX idx_rental_agreements_status ON rental_agreements(status);
