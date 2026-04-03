CREATE SCHEMA IF NOT EXISTS op;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE op.orders (
                           id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                           customer_id     UUID NOT NULL,
                           status          VARCHAR(30) NOT NULL DEFAULT 'CREATED',
                           total_amount    NUMERIC(10, 2) NOT NULL CHECK (total_amount >= 0),
                           created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                           updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),

                           CONSTRAINT chk_orders_status CHECK (status IN (
                                                                          'CREATED',
                                                                          'PAYMENT_PENDING',
                                                                          'PAYMENT_APPROVED',
                                                                          'PAYMENT_REJECTED',
                                                                          'PREPARING',
                                                                          'READY',
                                                                          'DELIVERED'
                               ))
);

CREATE TABLE op.order_items (
                                id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                order_id    UUID NOT NULL REFERENCES op.orders(id) ON DELETE CASCADE,
                                product_id  UUID NOT NULL,
                                name        VARCHAR(255) NOT NULL,
                                quantity    INT NOT NULL CHECK (quantity > 0),
                                unit_price  NUMERIC(10, 2) NOT NULL CHECK (unit_price >= 0)
);

CREATE TABLE op.order_events (
                                 id           UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                 order_id     UUID NOT NULL REFERENCES op.orders(id) ON DELETE CASCADE,
                                 event_type   VARCHAR(50) NOT NULL,
                                 payload      JSONB,
                                 occurred_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_orders_customer_id  ON op.orders(customer_id);
CREATE INDEX idx_orders_status       ON op.orders(status);
CREATE INDEX idx_order_items_order_id ON op.order_items(order_id);
CREATE INDEX idx_order_events_order_id ON op.order_events(order_id);
CREATE INDEX idx_order_events_type   ON op.order_events(event_type);

CREATE OR REPLACE FUNCTION op.set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_orders_updated_at
    BEFORE UPDATE ON op.orders
    FOR EACH ROW
    EXECUTE FUNCTION op.set_updated_at();