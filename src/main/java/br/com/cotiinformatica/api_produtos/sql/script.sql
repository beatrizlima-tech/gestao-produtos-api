-- SCRIP PARA CRIAÇÃO DA TABELA DE PRODUTOS

CREATE TABLE produtos (

    id                      SERIAL                      PRIMARY KEY,
    nome                    VARCHAR(150)                NOT NULL,
    descricao               VARCHAR(500),
    preco                   DECIMAL(10,2)               NOT NULL,
    quantidade              INTEGER                     NOT NULL,
    data_cadastro           TIMESTAMP                   DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao        TIMESTAMP,
    data_exclusao           TIMESTAMP,

    CONSTRAINT chk_preco
                      CHECK ( preco > 0 ),

    CONSTRAINT chk_quantidade
                      CHECK ( quantidade >=0 )

);