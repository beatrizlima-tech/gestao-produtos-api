# 📦 Gestão de Produtos — API REST

API REST desenvolvida com **Java 21 e Spring Boot 4** para gerenciamento de produtos e controle de estoque.

O projeto integra uma aplicação **Full Stack**, com frontend desenvolvido em Angular e persistência de dados em PostgreSQL.

Além das operações CRUD, a API implementa controle de estoque, registro automático de datas e uma lixeira com exclusão lógica, restauração e limpeza agendada após 30 dias.

## 🛠️ Tecnologias

- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA / Hibernate
- PostgreSQL
- Jakarta Bean Validation
- Swagger / OpenAPI
- Maven
- Lombok
- JUnit 5
- Mockito
- MockMvc
- H2 Database (ambiente de testes)
- Spring Scheduling

## 🚀 Funcionalidades

**Gerenciamento de produtos**
- Cadastro de produtos com nome, descrição, preço e quantidade
- Listagem de produtos ativos
- Consulta individual por ID
- Atualização das informações cadastradas
- Cálculo automático do valor total por produto
- Validação dos dados de entrada

**Controle de datas**
- Registro automático da data de cadastro
- Atualização automática da data da última modificação
- Registro da data de exclusão lógica

**Lixeira de produtos**
- Exclusão lógica (*Soft Delete*), preservando o registro no banco
- Consulta dos produtos enviados à lixeira
- Restauração dentro do prazo de 30 dias
- Bloqueio de edição de produtos excluídos
- Exclusão definitiva de produtos na lixeira
- Limpeza automática diária dos registros com prazo expirado

**Qualidade e documentação**
- Tratamento de exceções e validações
- Documentação interativa com Swagger UI
- Testes automatizados com JUnit, Mockito, MockMvc e H2
- Integração REST com frontend Angular

## 🏗️ Arquitetura

A aplicação utiliza uma arquitetura em camadas, separando as responsabilidades:

**Controller → Service → Repository → Spring Data JPA / Hibernate → PostgreSQL**

- **Controller:** recebe as requisições HTTP e devolve as respostas da API.
- **Service:** implementa as regras de negócio.
- **Repository:** realiza operações de persistência e consulta.
- **Entity:** representa os produtos armazenados no banco.
- **DTOs:** definem os dados recebidos e devolvidos pela API.
- **Scheduler:** executa periodicamente a rotina de limpeza da lixeira.

A aplicação utiliza o `GlobalExceptionHandler` para centralizar o tratamento de erros de validação e recursos não encontrados, além do tratamento de conflitos operacionais no Controller.

## 🗑️ Funcionamento da lixeira

A exclusão de produtos utiliza o conceito de *Soft Delete*.

Ao solicitar uma exclusão, o sistema registra a data e a hora da operação no campo `dataExclusao`, sem remover imediatamente o registro do banco.

Enquanto estiver na lixeira, o produto não aparece na listagem de estoque ativo.

O produto poderá ser restaurado antes de completar 30 dias desde sua exclusão.

Após esse prazo, a restauração é bloqueada e o produto fica elegível à exclusão definitiva.

A rotina automática utiliza o `@Scheduled` do Spring Boot, com execução programada diariamente às **03h (horário de Brasília)**.

A tarefa é executada enquanto a aplicação estiver em funcionamento. A remoção poderá ocorrer na primeira execução agendada posterior ao vencimento do prazo.

## 🔗 Endpoints

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/v1/produtos` | Listar produtos ativos |
| GET | `/api/v1/produtos/{id}` | Buscar produto ativo por ID |
| POST | `/api/v1/produtos` | Cadastrar produto |
| PUT | `/api/v1/produtos/{id}` | Atualizar produto |
| DELETE | `/api/v1/produtos/{id}` | Enviar produto à lixeira |
| GET | `/api/v1/produtos/lixeira` | Consultar produtos excluídos |
| PATCH | `/api/v1/produtos/{id}/restaurar` | Restaurar produto |
| DELETE | `/api/v1/produtos/{id}/definitivo` | Excluir permanentemente |

## 📝 Exemplo de requisição

**POST `/api/v1/produtos`**

```json
{
  "nome": "Notebook Lenovo",
  "descricao": "Notebook para trabalho e estudos",
  "preco": 3500.00,
  "quantidade": 5
}
```

**Exemplo ilustrativo de resposta — HTTP 201 Created**

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

## ✅ Validações

- Nome obrigatório, com até 150 caracteres
- Descrição opcional, com até 500 caracteres
- Preço obrigatório, a partir de R$ 0,01
- Quantidade obrigatória e não negativa
- Rejeição de dados incompatíveis com o formato esperado
- Bloqueio de operações incompatíveis com o estado do produto
- Validação do prazo de recuperação da lixeira

## 📡 Respostas HTTP

| Código | Significado |
|---|---|
| 200 | Consulta, atualização, restauração ou exclusão lógica realizada |
| 201 | Produto cadastrado |
| 204 | Exclusão definitiva concluída, sem corpo na resposta |
| 400 | Dados ou JSON inválidos |
| 404 | Produto não encontrado |
| 409 | Conflito com as regras de negócio |

## 🧪 Testes automatizados

O projeto utiliza testes de integração e testes unitários.

**Testes de integração — JUnit e MockMvc**

- Cadastro de produtos
- Listagem de produtos
- Consulta por ID
- Atualização de produtos
- Exclusão lógica

**Testes unitários — JUnit e Mockito**

- Verificação da chamada de exclusão dos produtos expirados
- Verificação do cálculo da data limite de 30 dias
- Verificação da chamada da Service pela Scheduler
- Verificação da exclusão definitiva de produtos da lixeira

O H2 Database é utilizado em memória no perfil de testes, isolando a execução do PostgreSQL principal.

**Resultado da última execução: 9 testes aprovados, 0 falhas e 0 erros.**

Para executar os testes:

```bash
./mvnw test
```

No Windows:

```powershell
.\mvnw.cmd test
```

## ⚙️ Como executar

### Pré-requisitos

- JDK 21 ou superior compatível
- PostgreSQL
- Maven ou Maven Wrapper

### 1. Clonar o repositório

```bash
git clone https://github.com/beatrizlima-tech/gestao-produtos-api.git
```

### 2. Configurar o PostgreSQL

Crie o banco de dados `bd-gestao-produtos` e configure a tabela `produtos` conforme o modelo de persistência da aplicação.

A estrutura do banco deve contemplar os campos de descrição, cadastro, atualização e exclusão lógica.

A conexão utiliza variáveis de ambiente:

- `DB_URL`: endereço JDBC do PostgreSQL
- `DB_USERNAME`: usuário do banco
- `DB_PASSWORD`: senha do banco

Exemplo no PowerShell:

```powershell
$env:DB_PASSWORD = "SUA_SENHA_LOCAL"
```

As configurações padrão de conexão estão disponíveis no arquivo `application.properties`.

**Nunca publique credenciais reais no repositório.**

O projeto disponibiliza um script SQL de referência em
`src/main/java/br/com/cotiinformatica/api_produtos/sql/script.sql`.

### 3. Executar a aplicação

```bash
./mvnw spring-boot:run
```

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

A API utiliza a porta **8081**.

## 📚 Swagger UI

Com a aplicação em execução, acesse:

http://localhost:8081/swagger-ui/index.html

Documentação OpenAPI:

http://localhost:8081/v3/api-docs

## 💻 Frontend

O frontend foi desenvolvido com Angular e consome os endpoints REST da API.

A integração das novas funcionalidades de descrição, datas e lixeira está em desenvolvimento.

**Repositório:**

https://github.com/beatrizlima-tech/gestao-produtos-web

## 👩‍💻 Desenvolvedora

**Beatriz Lima**

GitHub: https://github.com/beatrizlima-tech

LinkedIn: https://www.linkedin.com/in/beatrizlima-tech