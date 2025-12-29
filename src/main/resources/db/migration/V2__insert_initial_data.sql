-- Migration V2: Insert initial test data
-- Data for product 35455 in brand 1 (ZARA) with different price lists and priorities

INSERT INTO prices (brand_id, start_date, end_date, price_list, product_id, priority, price, curr)
VALUES
-- Price list 1: Base price for the entire period (lowest priority)
(1, '2020-06-14 00:00:00', '2020-12-31 23:59:59', 1, 35455, 0, 35.50, 'EUR'),

-- Price list 2: Special price on June 14th from 3PM to 6:30PM (higher priority)
(1, '2020-06-14 15:00:00', '2020-06-14 18:30:00', 2, 35455, 1, 25.45, 'EUR'),

-- Price list 3: Special price on June 15th from midnight to 11AM (higher priority)
(1, '2020-06-15 00:00:00', '2020-06-15 11:00:00', 3, 35455, 1, 30.50, 'EUR'),

-- Price list 4: Special price from June 15th 4PM onwards (higher priority)
(1, '2020-06-15 16:00:00', '2020-12-31 23:59:59', 4, 35455, 1, 38.95, 'EUR');