ALTER TABLE order_history DROP CONSTRAINT if exists fk_order_history_order;
ALTER TABLE order_history ADD CONSTRAINT fk_order_history_order FOREIGN KEY (order_id) REFERENCES
"order" (id) ON DELETE CASCADE