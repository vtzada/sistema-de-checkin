ALTER TABLE evento ADD COLUMN banner_url VARCHAR(500);
ALTER TABLE evento ADD COLUMN categoria VARCHAR(30);

UPDATE evento SET categoria = 'SOCIAL' WHERE categoria IS NULL;

ALTER TABLE evento ALTER COLUMN categoria SET NOT NULL;