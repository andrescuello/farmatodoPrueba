
-- ================================
-- Farmatodo Challenge - schema.sql
-- ================================
DROP TABLE IF EXISTS audit_logs CASCADE;
DROP TABLE IF EXISTS payment_attempts CASCADE;
DROP TABLE IF EXISTS order_items CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS cart_items CASCADE;
DROP TABLE IF EXISTS tokenized_cards CASCADE;
DROP TABLE IF EXISTS search_events CASCADE;
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS customers CASCADE;

CREATE TABLE customers (
  id           UUID PRIMARY KEY,
  name         VARCHAR(120)   NOT NULL,
  email        VARCHAR(255)   NOT NULL UNIQUE,
  phone        VARCHAR(50)    NOT NULL UNIQUE,
  address      VARCHAR(255),
  created_at   TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE products (
  id           UUID PRIMARY KEY,
  name         VARCHAR(180)   NOT NULL,
  description  TEXT,
  price        NUMERIC(12,2)  NOT NULL CHECK (price >= 0),
  stock        INT            NOT NULL CHECK (stock >= 0),
  created_at   TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_products_name ON products (name);
CREATE INDEX IF NOT EXISTS idx_products_stock ON products (stock);

CREATE TABLE tokenized_cards (
  id           UUID PRIMARY KEY,
  token        VARCHAR(64)    NOT NULL UNIQUE,
  last4        CHAR(4)        NOT NULL,
  brand        VARCHAR(32)    NOT NULL,
  enc_pan      TEXT           NOT NULL,
  customer_id  UUID,
  created_at   TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_tokenized_cards_customer
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE SET NULL
);
CREATE INDEX IF NOT EXISTS idx_tokenized_cards_customer ON tokenized_cards (customer_id);

CREATE TABLE cart_items (
  id           UUID PRIMARY KEY,
  customer_id  UUID          NOT NULL,
  product_id   UUID          NOT NULL,
  qty          INT           NOT NULL CHECK (qty > 0),
  created_at   TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_cart_customer
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
  CONSTRAINT fk_cart_product
    FOREIGN KEY (product_id)  REFERENCES products(id),
  CONSTRAINT uq_cart_customer_product UNIQUE (customer_id, product_id)
);
CREATE INDEX IF NOT EXISTS idx_cart_customer ON cart_items (customer_id);
CREATE INDEX IF NOT EXISTS idx_cart_product ON cart_items (product_id);

CREATE TABLE orders (
  id               UUID PRIMARY KEY,
  customer_id      UUID           NOT NULL,
  shipping_address VARCHAR(255),
  status           VARCHAR(16)    NOT NULL,
  token_used       VARCHAR(64),
  created_at       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_orders_customer
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);
CREATE INDEX IF NOT EXISTS idx_orders_customer ON orders (customer_id);
CREATE INDEX IF NOT EXISTS idx_orders_created_at ON orders (created_at DESC);

CREATE TABLE order_items (
  id           UUID PRIMARY KEY,
  order_id     UUID           NOT NULL,
  product_id   UUID           NOT NULL,
  qty          INT            NOT NULL CHECK (qty > 0),
  unit_price   NUMERIC(12,2)  NOT NULL CHECK (unit_price >= 0),
  created_at   TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_order_items_order
    FOREIGN KEY (order_id)   REFERENCES orders(id)   ON DELETE CASCADE,
  CONSTRAINT fk_order_items_product
    FOREIGN KEY (product_id) REFERENCES products(id)
);
CREATE INDEX IF NOT EXISTS idx_order_items_order ON order_items (order_id);
CREATE INDEX IF NOT EXISTS idx_order_items_product ON order_items (product_id);

CREATE TABLE payment_attempts (
  id          UUID PRIMARY KEY,
  order_id    UUID        NOT NULL,
  attempt_no  INT         NOT NULL,
  approved    BOOLEAN     NOT NULL,
  reason      VARCHAR(64),
  at          TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_payment_attempts_order
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
  CONSTRAINT uq_payment_attempt UNIQUE (order_id, attempt_no)
);
CREATE INDEX IF NOT EXISTS idx_payment_attempts_order ON payment_attempts (order_id);

CREATE TABLE search_events (
  id             UUID PRIMARY KEY,
  q              VARCHAR(255)   NOT NULL,
  customer_email VARCHAR(255),
  at             TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_search_events_at ON search_events (at);
CREATE INDEX IF NOT EXISTS idx_search_events_q ON search_events (q);

CREATE TABLE audit_logs (
  id         UUID PRIMARY KEY,
  trace_id   VARCHAR(64)  NOT NULL,
  event_type VARCHAR(64)  NOT NULL,
  payload    TEXT,
  at         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_audit_logs_trace_id ON audit_logs (trace_id);
CREATE INDEX IF NOT EXISTS idx_audit_logs_event_at ON audit_logs (event_type, at);
