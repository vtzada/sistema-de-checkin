
-- USUARIO

CREATE TABLE usuario
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    role  VARCHAR(20)  NOT NULL,

    CONSTRAINT uq_usuario_email UNIQUE (email),
    CONSTRAINT ck_usuario_role CHECK (role IN ('ADMIN', 'PRODUTOR', 'PORTARIA'))
);

-- EVENTO

CREATE TABLE evento
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome      VARCHAR(255) NOT NULL,
    data      TIMESTAMP,
    local     VARCHAR(255) NOT NULL,
    ativo     BOOLEAN      NOT NULL DEFAULT FALSE,
    criado_em TIMESTAMP    NOT NULL DEFAULT now()
);

-- PACOTE

CREATE TABLE pacote
(
    id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome              VARCHAR(255),
    limite_jogadores  INTEGER   NOT NULL,
    limite_vips       INTEGER   NOT NULL,
    limite_convidados INTEGER   NOT NULL,
    criado_em         TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT ck_pacote_limites_nao_negativos CHECK (
        limite_jogadores >= 0 AND
        limite_vips >= 0 AND
        limite_convidados >= 0
        )
);

-- PATROCINADOR

CREATE TABLE patrocinador
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome      VARCHAR(255),
    evento_id BIGINT,
    pacote_id BIGINT,
    ativo     BOOLEAN   NOT NULL DEFAULT FALSE,
    criado_em TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT fk_patrocinador_evento
        FOREIGN KEY (evento_id) REFERENCES evento (id) ON DELETE RESTRICT,
    CONSTRAINT fk_patrocinador_pacote
        FOREIGN KEY (pacote_id) REFERENCES pacote (id) ON DELETE RESTRICT
);

-- CONVIDADO

CREATE TABLE convidado
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome_completo      VARCHAR(255) NOT NULL,
    email              VARCHAR(255) NOT NULL,
    tipo_convidado     VARCHAR(20)  NOT NULL,
    patrocinador_id    BIGINT       NOT NULL,
    status_confirmacao VARCHAR(20),
    token_confirmacao  VARCHAR(255),

    CONSTRAINT fk_convidado_patrocinador
        FOREIGN KEY (patrocinador_id) REFERENCES patrocinador (id) ON DELETE RESTRICT,
    CONSTRAINT ck_convidado_tipo CHECK (
        tipo_convidado IN ('VIP', 'JOGADOR', 'CONVIDADO_COMUM')
        ),
    CONSTRAINT ck_convidado_status_confirmacao CHECK (
        status_confirmacao IS NULL OR status_confirmacao IN ('PENDENTE', 'CONFIRMADO')
        )
);

-- INGRESSOS

CREATE TABLE ingressos
(
    id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    codigo_qr         UUID      NOT NULL,
    status            VARCHAR(20),
    data_hora_entrada TIMESTAMP,
    convidado_id      BIGINT    NOT NULL,
    criado_em         TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT uq_ingressos_codigo_qr UNIQUE (codigo_qr),
    CONSTRAINT fk_ingressos_convidado
        FOREIGN KEY (convidado_id) REFERENCES convidado (id) ON DELETE RESTRICT,
    CONSTRAINT ck_ingressos_status CHECK (
        status IS NULL OR status IN ('VALIDO', 'UTILIZADO', 'CANCELADO')
        )
);

-- ÍNDICES DE APOIO (FKs não geram índice automático no Postgres)

CREATE INDEX idx_patrocinador_evento_id ON patrocinador (evento_id);
CREATE INDEX idx_patrocinador_pacote_id ON patrocinador (pacote_id);
CREATE INDEX idx_convidado_patrocinador_id ON convidado (patrocinador_id);
CREATE INDEX idx_ingressos_convidado_id ON ingressos (convidado_id);