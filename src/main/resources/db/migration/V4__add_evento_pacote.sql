ALTER TABLE pacote
    ADD COLUMN evento_id BIGINT NOT NULL REFERENCES evento(id) ON DELETE RESTRICT;

CREATE INDEX idx_pacote_evento_id ON pacote (evento_id);
CREATE INDEX idx_evento_usuario_id ON evento (usuario_id);