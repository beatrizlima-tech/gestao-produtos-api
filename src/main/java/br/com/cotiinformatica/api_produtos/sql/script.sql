-- SCRIPT PARA CRIAÇÃO DA TABELA DE PRODUTOS

CREATE TABLE produtos (
    id                      SERIAL                  PRIMARY KEY,
    nome                    VARCHAR(150)            NOT NULL,
    descricao               VARCHAR(500)            NULL,
    preco                   DECIMAL(10,2)           NOT NULL,
    quantidade              INTEGER                 NOT NULL,
    categoria               VARCHAR(30)             NOT NULL,
    data_cadastro           TIMESTAMP               DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_categoria
    CHECK (categoria IN(
        'INFORMATICA',
        'ELETRONICOS',
        'VESTUARIO',
        'LIVRARIA',
        'OUTROS'
        )),

    CONSTRAINT chk_preco
    CHECK (preco > 0 ),

    CONSTRAINT chk_quantidade
    CHECK (quantidade >= 0)

);
