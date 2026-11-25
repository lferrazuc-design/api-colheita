# API Colheita 🌾

API REST para controle de **frentes de colheita de cana-de-açúcar**: cadastro das frentes, fornecedor responsável, capacidade diária (toneladas/dia) e status operacional.

O projeto nasceu de um problema real: acompanhar dezenas de frentes e fornecedores ao mesmo tempo, em planilhas, durante a safra.

## Tecnologias

- **Java 21** e **Spring Boot 3**
- **Spring Data JPA** + **PostgreSQL**
- **Bean Validation** para validar a entrada
- **JUnit 5**, **Mockito** e **MockMvc** para os testes (com H2 em memória)
- **Docker** e **Docker Compose**
- **Swagger / OpenAPI** para documentação
- **GitHub Actions** para CI

## Arquitetura

```
controller  →  recebe as requisições HTTP e valida a entrada
service     →  regras de negócio (ex: não permitir código duplicado)
repository  →  acesso ao banco de dados
model       →  entidades JPA
dto         →  objetos de entrada (Request) e saída (Response)
exception   →  tratamento centralizado de erros (ProblemDetail / RFC 7807)
```

## Como rodar

### Opção 1: tudo no Docker
```bash
docker compose --profile completo up --build
```

### Opção 2: banco no Docker, API no IntelliJ
```bash
docker compose up -d db
```
Depois rode a classe `ApiColheitaApplication` no IntelliJ.

A API sobe em `http://localhost:8081`.

Ao iniciar, a aplicação carrega **10 frentes fictícias** (F-01 a F-10, com fornecedores inventados) para você já testar com dados. Todos os nomes e números são de exemplo. Para desligar, use `app.dados-exemplo=false` no `application.properties`.

- Documentação (Swagger): http://localhost:8081/swagger-ui.html
- Health check: http://localhost:8081/actuator/health

## Endpoints

| Método | Rota             | Descrição                                   |
|--------|------------------|---------------------------------------------|
| POST   | `/frentes`       | Cria uma frente                             |
| GET    | `/frentes`       | Lista as frentes (filtro: `?status=ATIVA`)  |
| GET    | `/frentes/{id}`  | Busca uma frente                            |
| PUT    | `/frentes/{id}`  | Atualiza uma frente                         |
| DELETE | `/frentes/{id}`  | Exclui uma frente                           |

Exemplo de criação:
```json
POST /frentes
{
  "codigo": "F-11",
  "fornecedor": "Fornecedor A",
  "toneladasDia": 850.0
}
```

Respostas de erro:
- `400` dados inválidos (com os campos que falharam)
- `404` frente não encontrada
- `409` código de frente já cadastrado

## Testes

```bash
mvn test
```

- `FrenteServiceTest`: testes unitários da regra de negócio (com mocks)
- `FrenteControllerTest`: testes de integração dos endpoints (com H2)

## Próximos passos

- [ ] Entidade `OrdemServico` relacionada à frente
- [ ] Paginação na listagem
- [ ] Migrações de banco com Flyway
- [ ] Publicar eventos no Kafka quando o status da frente mudar
