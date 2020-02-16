create table event_store (
  id VARCHAR(128) NOT NULL,
  json_value LONGTEXT,
  created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);