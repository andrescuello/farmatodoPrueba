
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

INSERT INTO products (id, name, description, price, stock)
VALUES
  (uuid_generate_v4(), 'Acetaminofén 500mg', 'Caja x 24', 6500, 120),
  (uuid_generate_v4(), 'Ibuprofeno 400mg', 'Caja x 10', 5200, 80);
