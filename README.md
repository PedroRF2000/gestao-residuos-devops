# Projeto - Cidades ESG Inteligentes

## Visão geral
Este projeto implementa uma API REST em Java Spring Boot para gestão de resíduos, coletas, transportadores, destinos finais e autenticação JWT. A entrega foi adaptada para um cenário DevOps com pipeline de CI/CD, containerização e orquestração com Docker Compose.

## Estrutura do projeto
```text
seu-projeto/
├── .github/workflows/ci-cd.yml
├── Dockerfile
├── docker-compose.yml
├── .env.example
├── .env.staging.example
├── .env.production.example
├── src/
├── docs/
└── README.md
```

## Como executar localmente com Docker
### 1. Pré-requisitos
- Docker
- Docker Compose

### 2. Configurar variáveis de ambiente
```bash
cp .env.example .env
```

### 3. Subir a aplicação
```bash
docker compose up -d --build
```

### 4. Validar funcionamento
- Swagger: `http://localhost:8080/api/swagger-ui.html`
- Healthcheck: `http://localhost:8080/api/actuator/health`

### 5. Parar containers
```bash
docker compose down
```

## Pipeline CI/CD
### Ferramenta utilizada
GitHub Actions.

### Etapas do pipeline
1. **Checkout do código**
2. **Setup do Java 17**
3. **Execução de testes automatizados** com Maven (`mvn test`)
4. **Build da aplicação** (`mvn clean package -DskipTests`)
5. **Build da imagem Docker**
6. **Deploy em staging** quando houver push na branch `develop`
7. **Deploy em produção** quando houver push na branch `main`

### Estratégia de deploy
O workflow está preparado para deploy remoto via SSH usando secrets do GitHub:
- `STAGING_HOST`, `STAGING_USER`, `STAGING_SSH_KEY`
- `PRODUCTION_HOST`, `PRODUCTION_USER`, `PRODUCTION_SSH_KEY`

Em cada ambiente, o pipeline copia os arquivos do projeto, aplica o `.env` correspondente e executa:
```bash
docker compose down
docker compose up -d --build
```

## Containerização
### Dockerfile adotado
```dockerfile
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B clean package

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN apk add --no-cache wget
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=5s --start-period=45s --retries=5 \
  CMD wget -qO- http://localhost:8080/api/actuator/health | grep '"status":"UP"' || exit 1
ENTRYPOINT ["java","-jar","/app/app.jar"]
```

### Estratégias adotadas
- **Build multi-stage** para reduzir a imagem final.
- **Java 17** alinhado ao projeto Spring Boot.
- **Healthcheck** com endpoint do Actuator.
- **Externalização de configuração** com variáveis de ambiente.

## Orquestração com Docker Compose
O `docker-compose.yml` sobe dois serviços:
- **oracle-db**: banco Oracle XE com volume persistente.
- **api**: aplicação Spring Boot conectada ao banco via rede interna Docker.

Recursos usados:
- **Volumes**: persistência de dados do Oracle.
- **Variáveis de ambiente**: credenciais e parâmetros da aplicação.
- **Rede dedicada**: comunicação entre aplicação e banco.
- **depends_on com healthcheck**: a API só sobe após o banco ficar saudável.

## Prints do funcionamento
Prints serão adicionados após execução final do pipeline e deploy.

## Tecnologias utilizadas
- Java 17
- Spring Boot 3
- Spring Security
- Spring Data JPA
- Flyway
- Oracle XE
- Maven
- Docker
- Docker Compose
- GitHub Actions
- JWT
- Swagger / OpenAPI

## Observações importantes
- Para o pipeline funcionar de verdade, os secrets do GitHub precisam ser cadastrados.
- Os arquivos `.env.staging` e `.env.production` reais não devem ir para o repositório.
- Antes da entrega, substitua os placeholders por prints reais dos ambientes.

## Checklist de Entrega
| Item | OK |
|---|---|
| Projeto compactado em .ZIP com estrutura organizada | ☒ |
| Dockerfile funcional | ☒ |
| docker-compose.yml ou arquivos Kubernetes | ☒ |
| Pipeline com etapas de build, teste e deploy | ☒ |
| README.md com instruções e prints | ☐ |
| Documentação técnica com evidências (PDF ou PPT) | ☒ |
| Deploy realizado nos ambientes staging e produção | ☐ |

> Marque o último item como `☒` somente se você tiver feito o deploy real e capturado as evidências.
