-- Create payments table
CREATE TABLE IF NOT EXISTS payments (
    id BIGSERIAL PRIMARY KEY,
    rental_id BIGINT NOT NULL,
    payment_type VARCHAR(50) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    transaction_hash VARCHAR(255),
    blockchain_status VARCHAR(50),
    payment_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_payments_rental_id ON payments(rental_id);
CREATE INDEX idx_payments_transaction_hash ON payments(transaction_hash);
