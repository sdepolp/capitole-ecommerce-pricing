
-- Migration V1: Create prices table
-- This table stores price information for products in different brands with validity periods

CREATE TABLE prices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    brand_id INTEGER NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    price_list INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    priority INTEGER NOT NULL,
    price DECIMAL(19,2) NOT NULL,
    curr VARCHAR(3) NOT NULL,

    CONSTRAINT chk_price_positive CHECK (price >= 0),
    CONSTRAINT chk_dates CHECK (start_date <= end_date),
    CONSTRAINT chk_brand_positive CHECK (brand_id > 0),
    CONSTRAINT chk_product_positive CHECK (product_id > 0)
);

-- Index to optimize queries by product_id, brand_id and date range
CREATE INDEX idx_prices_lookup
ON prices(product_id, brand_id, start_date, end_date);

-- Additional index for priority ordering
CREATE INDEX idx_prices_priority
ON prices(priority DESC);