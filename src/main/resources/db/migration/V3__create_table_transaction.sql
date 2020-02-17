create table transaction_store (
  id VARCHAR(128) NOT NULL,
  fromAccount VARCHAR(128) NOT NULL,
  toAccount VARCHAR(128) NOT NULL,
  amount DECIMAL(13, 2) NOT NULL default 0,
  created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);