
# 📦 Gestão de Produtos — API REST

API REST desenvolvida com Java e Spring Boot para gerenciamento
de produtos e controle de estoque.

O projeto integra um sistema Full Stack com frontend em Angular
e persistência de dados em PostgreSQL.

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

## 🚀 Funcionalidades

- Cadastro de produtos
- Listagem de produtos
- Consulta de produto por ID
- Atualização de produtos
- Exclusão de produtos
- Cálculo do valor total por produto
- Validação de nome, preço e quantidade
- Tratamento centralizado de exceções
- Documentação interativa com Swagger UI
- Integração com frontend Angular

## 🏗️ Arquitetura

A aplicação utiliza arquitetura em camadas:

Controller
↓
Service
↓
Repository
↓
Spring Data JPA / Hibernate
↓
PostgreSQL

Os DTOs são utilizados para entrada e saída dos dados.

O GlobalExceptionHandler centraliza o tratamento
dos erros de validação, JSON inválido e recursos não encontrados.

## 🔗 Endpoints

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | /api/v1/produtos | Listar produtos |
| GET | /api/v1/produtos/{id} | Buscar produto por ID |
| POST | /api/v1/produtos | Cadastrar produto |
| PUT | /api/v1/produtos/{id} | Atualizar produto |
| DELETE | /api/v1/produtos/{id} | Excluir produto |

## 📝 Exemplo de requisição

POST /api/v1/produtos

```json
{
  "nome": "Notebook Lenovo",
  "preco": 3500.00,
  "quantidade": 5
}
```

Exemplo de resposta:

```json
{
  "id": 52,
  "nome": "Notebook Lenovo",
  "preco": 3500.0,
  "quantidade": 5,
  "total": 17500.0
}
```

O ID é gerado automaticamente pelo banco de dados.

## ✅ Validações

- Nome obrigatório, com até 150 caracteres
- Preço obrigatório, a partir de R$ 0,01
- Quantidade obrigatória e não negativa
- Rejeição de dados incompatíveis com o formato esperado

## 📡 Respostas HTTP

| Código | Significado |
|--------|-------------|
| 200 | Consulta, atualização ou exclusão realizada |
| 201 | Produto cadastrado |
| 400 | Dados ou JSON inválidos |
| 404 | Produto não encontrado |

As respostas de erro são padronizadas pelo tratamento
global de exceções.

## ⚙️ Como executar

### Pré-requisitos

- JDK 21 ou superior compatível
- Maven
- PostgreSQL

### 1. Clonar o repositório

```bash
git clone https://github.com/beatrizlima-tech/gestao-produtos-api.git
```

### 2. Configurar o PostgreSQL

Crie o banco de dados indicado na configuração
`spring.datasource.url` do projeto.

Defina a variável de ambiente DB_PASSWORD com
a senha do seu PostgreSQL.

Exemplo no PowerShell:

```powershell
$env:DB_PASSWORD = "SUA_SENHA_LOCAL"
```

Nunca publique senhas reais no GitHub.

### 3. Executar a aplicação

```bash
mvn spring-boot:run
```

A aplicação utiliza a porta 8081.

## 📚 Swagger UI

Com a API em execução:

http://localhost:8081/swagger-ui/index.html

Documentação OpenAPI:

http://localhost:8081/v3/api-docs

## 💻 Frontend

O frontend foi desenvolvido em Angular.

Repositório:
https://github.com/beatrizlima-tech/gestao-produtos-web

## 👩‍💻 Desenvolvedora

Beatriz Lima

GitHub: https://github.com/beatrizlima-tech

LinkedIn: https://www.linkedin.com/in/beatrizlima-tech
