--Create Customers
INSERT INTO customer (id, name, email) VALUES 
(1, 'Laxmi', 'laxmi.p@email.com'),
(2, 'Rahul', 'rahul.h@email.com'),
(3, 'Krish', 'krish@email.com');

-- Create Transactions for last 3 months
-- Customer 1 Transactions
INSERT INTO transaction (customer_id, amount, date) VALUES
(1, 120.00, CURRENT_DATE - 5),  -- 90 points
(1, 85.00, CURRENT_DATE - 15),  -- 35 points
(1, 160.00, CURRENT_DATE - 45), -- 170 points
(1, 45.00, CURRENT_DATE - 55),  -- 0 points
(1, 200.00, CURRENT_DATE - 65); -- 250 points

-- Customer 2 Transactions
INSERT INTO transaction (customer_id, amount, date) VALUES
(2, 90.00, CURRENT_DATE - 10),  -- 40 points
(2, 110.00, CURRENT_DATE - 20), -- 70 points
(2, 50.00, CURRENT_DATE - 40),  -- 0 points
(2, 130.00, CURRENT_DATE - 60); -- 110 points

-- Customer 3 Transactions
INSERT INTO transaction (customer_id, amount, date) VALUES
(3, 150.00, CURRENT_DATE - 5),  -- 150 points
(3, 95.00, CURRENT_DATE - 25),  -- 45 points
(3, 180.00, CURRENT_DATE - 45), -- 210 points
(3, 65.00, CURRENT_DATE - 75);  -- 15 points

-- Create indexes for better performance
CREATE INDEX idx_transaction_customer_date ON transaction(customer_id, date);
CREATE INDEX idx_customer_email ON customer(email);