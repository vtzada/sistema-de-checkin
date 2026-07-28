ALTER TABLE usuario DROP CONSTRAINT ck_usuario_role;

ALTER TABLE usuario ADD CONSTRAINT ck_usuario_role CHECK (role IN ('ADMIN', 'PRODUTOR', 'PORTARIA', 'CLIENTE'));