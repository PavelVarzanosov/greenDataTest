-- DROP TABLE IF EXISTS Widget;
--
-- CREATE TABLE Widget (
--   id UUID  PRIMARY KEY,
--   x int NOT NULL,
--   y int NOT NULL,
--   width int NOT NULL,
--   height int NOT NULL,
--   zIndex int NOT NULL,
--   LastDTFormatted TIMESTAMP NOT NULL
-- );

INSERT INTO Bank (ID, NAME, BIK) VALUES
  (RANDOM_UUID(), 'SBERBANK', 10),
  (RANDOM_UUID(), 'VTB', 100),
  (RANDOM_UUID(), 'TINKOFF', 5),
  (RANDOM_UUID(), 'NONAME', 200),
  (RANDOM_UUID(), 'SOVKOMBANK', 200);