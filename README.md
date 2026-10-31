
# 📦 Gestão de Produtos — API REST

![Java 21](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot 4.0.8](https://img.shields.io/badge/Spring_Boot-4.0.8-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![PostgreSQL 16](https://img.shields.io/badge/PostgreSQL-16-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)

![Flyway](https://img.shields.io/badge/Flyway-Migrations-CC0200?style=for-the-badge&logo=flyway&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Testcontainers](https://img.shields.io/badge/Testcontainers-PostgreSQL-2563EB?style=for-the-badge)
![Testes](https://img.shields.io/badge/Testes-12%20aprovados-2EA44F?style=for-the-badge)

API REST desenvolvida para gerenciamento de produtos e controle de estoque, com operações CRUD, validações, registro automático de datas e uma lixeira com restauração e limpeza agendada.

O projeto faz parte de uma aplicação **Full Stack**, com frontend desenvolvido em Angular e persistência de dados em PostgreSQL.

---

## 🛠️ Tecnologias

### Back-end

- Java 21
- Spring Boot 4.0.8
- Spring Web MVC
- Spring Data JPA / Hibernate
- Jakarta Bean Validation
- Spring Scheduling
- Maven
- Lombok

### Banco de dados e infraestrutura

- PostgreSQL 16
- Flyway
- Docker
- Docker Compose
- pgAdmin

### Testes e documentação

- JUnit 5
- Mockito
- MockMvc
- H2 Database
- Testcontainers
- Swagger / OpenAPI

---

## 🚀 Funcionalidades

### 📦 Gerenciamento de produtos

- Cadastro com nome, descrição, preço e quantidade.
- Listagem dos produtos ativos.
- Consulta individual por ID.
- Atualização das informações cadastradas.
- Cálculo automático do valor total por produto.
- Validação dos dados recebidos pela API.

### 📅 Controle de datas

- Registro automático da data de cadastro.
- Atualização da data da última modificação.
- Registro da data de exclusão lógica.

### 🗑️ Lixeira de produtos

- Exclusão lógica (*soft delete*), preservando o registro no banco.
- Consulta dos produtos excluídos.
- Restauração dentro do prazo de 30 dias.
- Bloqueio de edição de produtos excluídos.
- Exclusão definitiva.
- Limpeza automática dos registros com prazo expirado.

### ✅ Qualidade e manutenção

- Tratamento centralizado de exceções.
- Testes unitários e de integração.
- Migrações SQL versionadas com Flyway.
- Testes de migração com PostgreSQL real utilizando Testcontainers.
- Documentação interativa com Swagger UI no ambiente de desenvolvimento.
- Integração REST com frontend Angular.

---

## 🏗️ Arquitetura

A aplicação utiliza uma arquitetura em camadas, separando as responsabilidades de cada componente.

```text
              Requisição HTTP
                     |
                     v
                Controller
                     |
                     v
                  Service
                     |
                     v
                Repository
                     |
                     v
             Spring Data JPA
                     |
                     v
                 PostgreSQL
```

### Responsabilidades

| Camada | Responsabilidade |
|---|---|
| Controller | Recebe as requisições HTTP e retorna as respostas da API. |
| Service | Implementa as regras de negócio. |
| Repository | Realiza consultas e operações de persistência. |
| Entity | Representa os produtos armazenados no banco. |
| DTOs | Definem os dados recebidos e devolvidos pela API. |
| Scheduler | Executa a rotina de limpeza da lixeira. |
| Exception Handler | Centraliza o tratamento de erros e exceções. |

O **Flyway** é responsável pelo versionamento das alterações estruturais do banco de dados.

---

## 🗑️ Funcionamento da lixeira

A exclusão de produtos utiliza o conceito de **soft delete**.

Ao solicitar a exclusão, o sistema registra a data e a hora no campo `dataExclusao`, sem remover imediatamente o produto do banco.

Enquanto estiver na lixeira, o registro não aparece na listagem de produtos ativos.

### ♻️ Restauração

O produto pode ser restaurado antes de completar **30 dias** desde sua exclusão.

Após esse prazo, a restauração é bloqueada e o registro fica elegível à exclusão definitiva.

### ⏰ Limpeza automática

A limpeza é realizada por meio do `@Scheduled` do Spring Boot.

O método responsável é `executarLimpeza()`, da classe `LixeiraScheduler`.

A expressão de agendamento utilizada é:

`0 0 3 * * *`

A rotina executa diariamente às **03h, no horário de Brasília**, utilizando a zona `America/Sao_Paulo`.

A execução depende de a aplicação estar funcionando no horário agendado. Registros expirados podem ser removidos na próxima execução da rotina.

---

## 🔗 Endpoints da API

URL base:

`/api/v1/produtos`

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/v1/produtos` | Listar produtos ativos |
| GET | `/api/v1/produtos/{id}` | Consultar produto por ID |
| POST | `/api/v1/produtos` | Cadastrar produto |
| PUT | `/api/v1/produtos/{id}` | Atualizar produto |
| DELETE | `/api/v1/produtos/{id}` | Enviar produto à lixeira |
| GET | `/api/v1/produtos/lixeira` | Consultar produtos excluídos |
| PATCH | `/api/v1/produtos/{id}/restaurar` | Restaurar produto |
| DELETE | `/api/v1/produtos/{id}/definitivo` | Excluir permanentemente |

---

## 📝 Exemplos de utilização

### Cadastrar produto

**POST** `/api/v1/produtos`

Exemplo de requisição:

```json
{
  "nome": "Notebook Lenovo",
  "descricao": "Notebook para trabalho e estudos",
  "preco": 3500.00,
  "quantidade": 5
}
```

Exemplo ilustrativo de resposta — **HTTP 201 Created**:

```json
{
  "id": 52,
  "nome": "Notebook Lenovo",
  "descricao": "Notebook para trabalho e estudos",
  "preco": 3500.0,
  "quantidade": 5,
  "total": 17500.0,
  "dataCadastro": "2026-10-22T10:30:00",
  "dataAtualizacao": "2026-10-22T10:30:00",
  "dataExclusao": null
}
```

O ID e as datas de cadastro e atualização são gerados automaticamente.

O campo `dataExclusao` permanece `null` enquanto o produto estiver ativo.

O valor de `total` é calculado a partir do preço unitário e da quantidade cadastrada.

### Consultar produtos ativos

**GET** `/api/v1/produtos`

Em um banco sem produtos cadastrados:

```json
[]
```

### Enviar produto à lixeira

**DELETE** `/api/v1/produtos/{id}`

O registro é marcado como excluído, mas permanece no banco de dados.

### Restaurar produto

**PATCH** `/api/v1/produtos/{id}/restaurar`

A restauração é permitida enquanto o produto estiver dentro do prazo de recuperação.

### Excluir definitivamente

**DELETE** `/api/v1/produtos/{id}/definitivo`

Quando a exclusão definitiva é concluída, a API retorna **HTTP 204 No Content**.

---

## ✅ Validações

A API utiliza Jakarta Bean Validation para validar os dados de entrada.

| Campo | Regra |
|---|---|
| Nome | Obrigatório, até 150 caracteres |
| Descrição | Opcional |
| Preço | Obrigatório, mínimo de R$ 0,01 |
| Precisão do preço | Até 8 dígitos inteiros e 2 casas decimais |
| Quantidade | Obrigatória, maior ou igual a zero |

**Observação:** o banco aceita descrições com até 500 caracteres, mas o DTO atualmente está configurado para validar no máximo 150 caracteres. Esse limite será alinhado em uma correção futura.

### Respostas HTTP

| Código | Significado |
|---|---|
| 200 | Operação realizada com sucesso |
| 201 | Produto cadastrado |
| 204 | Exclusão definitiva concluída |
| 400 | Dados ou JSON inválidos |
| 404 | Produto não encontrado |
| 409 | Conflito com regras de negócio |

---

## 🗃️ Versionamento do banco com Flyway

O projeto utiliza **Flyway** para controlar a evolução da estrutura do PostgreSQL por meio de migrações SQL versionadas.

As migrações ficam no diretório:

```text
src/main/resources/db/migration/
```

### Migração inicial — V1

Arquivo:

```text
V1__criar_tabela_produtos.sql
```

A migração inicial cria:

- A tabela `produtos`.
- A sequência `produtos_seq`.
- A chave primária.
- As colunas de cadastro, atualização e exclusão.
- Os campos necessários ao gerenciamento dos produtos.

A sequência utiliza incremento de **50**, compatível com a configuração de geração de IDs utilizada pelo Hibernate.

### Como funciona

Em um **banco novo e vazio**, o Flyway executa a migração V1 automaticamente durante a inicialização da aplicação.

O histórico das migrações é armazenado na tabela:

```text
flyway_schema_history
```

Esse histórico permite identificar quais versões já foram aplicadas.

### Bancos existentes

Bancos que já possuem a tabela `produtos`, mas não possuem histórico do Flyway, exigem um procedimento de adoção controlado.

Antes de registrar uma versão inicial, é necessário:

1. Realizar um backup do banco.
2. Conferir a estrutura existente.
3. Verificar sua compatibilidade com a migração inicial.
4. Registrar explicitamente o baseline na versão 1.
5. Validar a inicialização da aplicação.

A opção `baseline-on-migrate` **não permanece ativada automaticamente**.

Esse cuidado evita que uma estrutura desconhecida seja registrada como compatível sem verificação.

### Próximas migrações

Novas alterações estruturais devem ser adicionadas como novas versões, por exemplo:

```text
V2__adicionar_nova_coluna.sql
V3__criar_nova_tabela.sql
```

As migrações já aplicadas não devem ser alteradas.

---

## 🧪 Testes automatizados

A aplicação utiliza **JUnit 5, Mockito, MockMvc, H2 e Testcontainers** para validar diferentes partes do sistema.

### Testes de integração

Os testes de integração verificam operações da API, incluindo:

- Cadastro de produtos.
- Listagem de produtos.
- Consulta por ID.
- Atualização.
- Exclusão lógica.
- Validações de entrada.

Os testes da API utilizam H2 em memória no perfil de testes.

### Testes unitários

Os testes unitários verificam regras de negócio, incluindo:

- Cálculo do prazo de 30 dias.
- Exclusão de produtos expirados.
- Delegação da rotina agendada para a camada de serviço.
- Comportamentos relacionados à exclusão definitiva.

### 🐳 Testes com Testcontainers

A classe `FlywayMigrationTest` utiliza um **PostgreSQL 16 temporário**, criado automaticamente pelo Testcontainers.

O teste verifica:

1. Execução da migração V1.
2. Criação da tabela `produtos`.
3. Existência das oito colunas esperadas.
4. Configuração da sequência `produtos_seq`.
5. Registro da migração no histórico do Flyway.
6. Ausência de nova execução da V1 após uma segunda chamada de migração.

O PostgreSQL temporário é independente do banco principal da aplicação.

### Resultado dos testes

**Última execução verificada: 12 testes aprovados, 0 falhas e 0 erros.**

### Executar os testes

No Windows:

```powershell
.\mvnw.cmd clean test
```

No Linux ou macOS:

```bash
./mvnw clean test
```

**Importante:** o Docker precisa estar em execução para o teste com Testcontainers funcionar.

---

## 🐳 Executando com Docker Compose

O projeto possui um ambiente configurado com Docker Compose, composto por três serviços:

- API de Gestão de Produtos.
- PostgreSQL 16.
- pgAdmin.

### Pré-requisitos

- Docker Desktop ou Docker Engine com Docker Compose.
- Git.

### 1. Clonar o repositório

```bash
git clone https://github.com/beatrizlima-tech/gestao-produtos-api.git
cd gestao-produtos-api
```

### 2. Configurar as variáveis de ambiente

Na raiz do projeto, crie um arquivo chamado `.env`.

Adicione:

```dotenv
POSTGRES_PASSWORD=substitua_por_uma_senha_forte
PGADMIN_PASSWORD=substitua_por_outra_senha_forte
```

Substitua os valores de exemplo por senhas próprias.

**Nunca publique senhas ou o arquivo `.env` no repositório.**

### 3. Iniciar os serviços

```bash
docker compose up -d --build
```

O comando constrói a imagem da aplicação e inicia os serviços configurados.

### 4. Acessar os serviços

| Serviço | Endereço local |
|---|---|
| API REST | `http://localhost:8086` |
| PostgreSQL | `localhost:5434` |
| pgAdmin | `http://localhost:5050` |

O banco configurado pelo Compose chama-se `apiprodutosdb`.

A API utiliza o perfil **prod** e executa na porta 8080 dentro do contêiner, publicada na porta 8086 do computador.

### 5. Verificar os contêineres

```bash
docker compose ps
```

### 6. Consultar os logs

```bash
docker logs --tail 100 api-produtos
```

### 7. Testar a API

Abra:

http://localhost:8086/api/v1/produtos

Ou execute:

```bash
curl http://localhost:8086/api/v1/produtos
```

Em um banco vazio, a resposta esperada é:

```json
[]
```

### 8. Encerrar os serviços

```bash
docker compose down
```

Os volumes são preservados para manter os dados do PostgreSQL e do pgAdmin.

**Atenção:** evite utilizar `docker compose down -v` se deseja preservar os dados, pois essa opção remove os volumes do projeto.

---

## ⚙️ Executando localmente com Maven

Também é possível executar a aplicação fora do Docker.

### Pré-requisitos

- JDK 21 ou superior compatível.
- PostgreSQL acessível.
- Maven Wrapper incluído no projeto.

### 1. Configurar a conexão

A aplicação utiliza as seguintes variáveis de ambiente:

| Variável | Finalidade |
|---|---|
| `DB_URL` | Endereço JDBC do PostgreSQL |
| `DB_USERNAME` | Usuário do banco |
| `DB_PASSWORD` | Senha do banco |

Exemplo no PowerShell, utilizando o PostgreSQL do Docker Compose:

```powershell
$env:DB_URL = "jdbc:postgresql://localhost:5434/apiprodutosdb"
$env:DB_USERNAME = "postgres"
$env:DB_PASSWORD = "SUA_SENHA_LOCAL"
```

Substitua `SUA_SENHA_LOCAL` pela senha configurada para o PostgreSQL.

### 2. Executar a aplicação

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

No Linux ou macOS:

```bash
./mvnw spring-boot:run
```

A configuração local padrão utiliza a porta **8081**.

### Perfis de execução

O projeto possui configurações específicas para diferentes ambientes:

| Perfil | Configuração |
|---|---|
| Desenvolvimento | PostgreSQL e Hibernate com `ddl-auto: update` |
| Testes | H2 em memória e Flyway desativado para os testes da API |
| Produção | PostgreSQL, Flyway e Hibernate com `ddl-auto: validate` |

O perfil de produção também desativa o Swagger UI e a exposição da especificação OpenAPI.

**Importante:** utilize bancos novos ou previamente preparados para o Flyway. Bancos existentes sem histórico de migrações devem passar pelo procedimento controlado de baseline.

---

## 📚 Swagger / OpenAPI

A aplicação disponibiliza documentação interativa no ambiente de desenvolvimento.

Com a API local executando na porta 8081:

**Swagger UI:**

http://localhost:8081/swagger-ui/index.html

**Especificação OpenAPI:**

http://localhost:8081/v3/api-docs

No perfil `prod`, utilizado pelo Docker Compose, esses endpoints estão desativados.

---

## 🔐 Dependências e segurança

As dependências do projeto são gerenciadas pelo Maven.

Entre as versões utilizadas estão:

| Dependência | Versão |
|---|---|
| Spring Boot | 4.0.8 |
| Springdoc OpenAPI | 3.0.3 |
| PostgreSQL JDBC | 42.7.14 |
| Jackson 3.x | 3.1.8 |
| Jackson Databind 2.x | 2.21.8 |
| Jackson Core 2.x | 2.21.8 |
| Testcontainers | 2.0.5 |

As versões de dependências transitivas do Jackson foram revisadas após análise de alertas de segurança.

O projeto utiliza testes automatizados e inspeção de dependências como parte do processo de manutenção.

---

## 💻 Frontend Angular

O projeto faz parte de uma aplicação Full Stack com frontend desenvolvido em Angular.

O frontend consome os endpoints REST para realizar operações relacionadas ao gerenciamento de produtos.

**Repositório do frontend:**

[Gestão de Produtos — Angular](https://github.com/beatrizlima-tech/gestao-produtos-web)

---

## 👩‍💻 Desenvolvedora

**Beatriz Lima**

- **GitHub:** [beatrizlima-tech](https://github.com/beatrizlima-tech)
- **LinkedIn:** [beatrizlima-tech](https://www.linkedin.com/in/beatrizlima-tech)

---

**📦 Gestão de Produtos — Projeto de desenvolvimento Full Stack com Java, Spring Boot, PostgreSQL e Angular.**
