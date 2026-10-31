CREATE SEQUENCE produtos_seq
    START WITH 1
    INCREMENT BY 50;

CREATE TABLE produtos (
                          id INTEGER NOT NULL,
                          data_atualizacao TIMESTAMP(6),
                          data_cadastro TIMESTAMP(6),
                          data_exclusao TIMESTAMP(6),
                          descricao VARCHAR(500),
                          nome VARCHAR(150) NOT NULL,
                          preco NUMERIC(10, 2) NOT NULL,
                          quantidade INTEGER NOT NULL,
                          CONSTRAINT produtos_pkey PRIMARY KEY (id)
);
