CREATE TABLE learning_items (
    id UUID PRIMARY KEY,
    title TEXT NOT NULL,
    category TEXT NOT NULL,
    description TEXT
);

ALTER TABLE assignments
RENAME COLUMN item_id TO learning_item_id;

ALTER TABLE assignments
ADD CONSTRAINT fk_assignments_learning_item
FOREIGN KEY (learning_item_id)
REFERENCES learning_items(id);