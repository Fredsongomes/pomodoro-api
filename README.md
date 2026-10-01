<h1 align="center">🍅 Pomodoro API</h1>

<p align="center">
  API REST de gerenciamento de tarefas, pensada como back-end para o app Pomodoro <a href="https://github.com/Fredsongomes/Projeto-Fokus-React">Fokus</a>.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-25-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 25" />
  <img src="https://img.shields.io/badge/Spring_Boot-4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot 4" />
  <img src="https://img.shields.io/badge/PostgreSQL-16-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL 16" />
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker" />
  <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black" alt="Swagger" />
  <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" alt="Maven" />
</p>

---

## 📌 Sobre o Projeto

A **Pomodoro API** é uma API REST para criar, listar, filtrar, atualizar e remover as tarefas que o usuário vai executar durante os ciclos de foco da Técnica Pomodoro.

Projeto desenvolvido durante meus estudos de Back-End com Java e Spring Boot na Alura, com foco em boas práticas de API REST: DTOs, validação de entrada, tratamento global de erros, status HTTP corretos e documentação com Swagger.

## ✨ Funcionalidades

- **CRUD de tarefas**: criar, listar, buscar por id, atualizar e remover
- **Filtro por status** de conclusão (`?completed=true|false`)
- **Validação** do título com mensagens de erro em português
- **Erros padronizados** em JSON para dados inválidos
- **Respostas HTTP semânticas**: `201` com header `Location`, `204` na remoção e `404` para tarefa inexistente
- **Documentação interativa** com Swagger / OpenAPI
- **Docker Compose** sobe o banco e a API com um comando

## 🛠️ Tecnologias & Ferramentas

| Tecnologia | Descrição |
| --- | --- |
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" width="20" /> **Java 25** | Linguagem (DTOs com `record`) |
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" width="20" /> **Spring Boot 4** | Spring Web MVC, Spring Data JPA e Bean Validation |
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/postgresql/postgresql-original.svg" width="20" /> **PostgreSQL 16** | Banco de dados relacional |
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/swagger/swagger-original.svg" width="20" /> **springdoc-openapi** | Documentação Swagger UI |
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/docker/docker-original.svg" width="20" /> **Docker / Compose** | Containers da API e do banco |
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/maven/maven-original.svg" width="20" /> **Maven** | Build e dependências (via Maven Wrapper) |

## 🚀 Como rodar

### Opção 1: Docker (recomendado)

Pré-requisito: [Docker](https://www.docker.com/) instalado.

```sh
git clone https://github.com/Fredsongomes/pomodoro-api.git
cd pomodoro-api
cp .env.example .env    # defina a senha do banco em DB_PASSWORD
docker compose up --build
```

O build da aplicação acontece dentro do container, então não é preciso ter Java instalado.

- API: <http://localhost:8080>
- Swagger: <http://localhost:8080/swagger-ui.html>

Para parar mantendo os dados, use `docker compose down`. Para apagar o banco também, use `docker compose down -v`.

### Opção 2: local

Pré-requisitos: **Java 25** e um **PostgreSQL** acessível.

O jeito mais simples é subir só o banco pelo Docker (ele fica na porta **5433** do host) e rodar a API pela IDE ou pelo terminal:

```sh
docker compose up -d postgres

# Linux/macOS
DB_URL=jdbc:postgresql://localhost:5433/pomodoro DB_PASSWORD=sua-senha ./mvnw spring-boot:run

# Windows (PowerShell)
$env:DB_URL="jdbc:postgresql://localhost:5433/pomodoro"; $env:DB_PASSWORD="sua-senha"; ./mvnw spring-boot:run
```

> O Spring **não lê o arquivo `.env`** sozinho. Ao rodar fora do Docker, defina as variáveis no terminal ou na Run Configuration da IDE. Sem elas, a API tenta conectar em `localhost:5432` com usuário e senha `postgres`.

A tabela `task` é criada automaticamente pelo Hibernate (`ddl-auto=update`).

### Variáveis de ambiente

Veja [`.env.example`](./.env.example).

| Variável | Descrição | Padrão |
| --- | --- | --- |
| `DB_PASSWORD` | Senha do Postgres (obrigatória no Docker) | `postgres` (local) |
| `DB_URL` | URL JDBC do banco | `jdbc:postgresql://localhost:5432/pomodoro` |
| `DB_USERNAME` | Usuário do banco | `postgres` |
| `API_PORT` | Porta da API no host (Docker) | `8080` |

### Testes

```sh
./mvnw test
```

> O teste atual sobe o contexto completo do Spring, por isso **precisa do banco rodando**. Para gerar o jar sem o banco, use `./mvnw package -DskipTests`.

## 📚 Endpoints

A documentação completa e testável está no **Swagger** (`/swagger-ui.html`). Resumo:

| Método | Rota | Descrição | Sucesso |
| --- | --- | --- | --- |
| `GET` | `/tasks` | Lista as tarefas (filtro opcional `?completed=true\|false`) | `200` |
| `GET` | `/tasks/{id}` | Busca uma tarefa por id | `200` / `404` |
| `POST` | `/tasks` | Cria uma tarefa | `201` |
| `PUT` | `/tasks/{id}` | Atualiza título e status | `200` / `404` |
| `DELETE` | `/tasks/{id}` | Remove uma tarefa | `204` / `404` |

### Exemplo

```jsonc
// POST /tasks
{ "title": "Estudar Spring Boot", "completed": false }

// 201 Created  (Location: /tasks/1)
{ "id": 1, "title": "Estudar Spring Boot", "completed": false }
```

O campo `completed` é opcional; quando não é enviado, vale `false`.

### Erro de validação

O título é obrigatório e deve ter entre 3 e 100 caracteres:

```jsonc
// POST /tasks  { "title": "ab" }
// 400 Bad Request
{ "errors": { "title": "O título deve ter entre 3 e 100 caracteres" } }
```

## 📁 Estrutura de Pastas

```
pomodoro-api/
├── src/main/java/br/com/alura/pomodoro/api/
│   ├── config/          # Configuração do OpenAPI (Swagger)
│   ├── controller/      # Endpoints REST
│   ├── dto/             # Records de entrada e saída da API
│   ├── exception/       # Tratamento global de erros
│   ├── model/           # Entidade JPA
│   ├── repository/      # Acesso a dados (Spring Data JPA)
│   └── PomodoroApiApplication.java
├── src/main/resources/
│   └── application.properties
├── src/test/            # Testes
├── .env.example
├── docker-compose.yml
├── Dockerfile
└── pom.xml
```

## 👤 Autor

Feito por **Fredson Gomes**.

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/fredson-gomes-a8082a338/)
[![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/Fredsongomes)
