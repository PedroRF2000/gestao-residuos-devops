# Documentação Técnica - Projeto Cidades ESG Inteligentes

## Integrantes
- Preencher nome(s) do(s) integrante(s)

## 1. Descrição do projeto
A aplicação escolhida foi a API de Gestão de Resíduos, construída com Java Spring Boot e adaptada para um fluxo DevOps completo. O objetivo é automatizar build, testes e deploy, além de garantir execução padronizada por meio de containers.

## 2. Pipeline CI/CD
### Ferramenta utilizada
GitHub Actions.

### Lógica do pipeline
- Push em `develop`: executa build, testes e deploy em **staging**.
- Push em `main`: executa build, testes e deploy em **produção**.
- Pull requests: executam build e testes para validação antes do merge.

### Etapas
1. Checkout
2. Setup do Java 17
3. Testes com Maven
4. Empacotamento da aplicação
5. Build da imagem Docker
6. Deploy remoto por SSH

## 3. Docker e orquestração
### Arquitetura
- Container 1: API Spring Boot
- Container 2: Oracle XE
- Rede Docker privada
- Volume persistente para o banco

### Principais comandos
```bash
docker compose up -d --build
docker compose ps
docker compose logs -f api
docker compose down
```

### Imagem criada
A imagem é construída a partir de um Dockerfile multi-stage:
- estágio 1: build com Maven
- estágio 2: runtime com JRE Alpine

## 4. Evidências esperadas
Inserir prints nesta seção:
- Pipeline executando build
- Testes automatizados aprovados
- Deploy em staging
- Deploy em produção
- Endpoint de healthcheck respondendo
- Swagger da aplicação em execução

## 5. Desafios encontrados e soluções
### Desafio 1: healthcheck inexistente
O projeto fazia referência ao endpoint `/actuator/health`, mas não possuía a dependência do Spring Boot Actuator.
**Solução:** foi adicionada a dependência `spring-boot-starter-actuator`.

### Desafio 2: inconsistência na conexão com o Oracle
A configuração anterior misturava SID/Service Name e usava credenciais de `system`.
**Solução:** o `docker-compose.yml` foi ajustado para usar `jdbc:oracle:thin:@oracle-db:1521/XEPDB1` com usuário de aplicação.

### Desafio 3: configuração rígida no `application.properties`
As credenciais estavam fixas no arquivo.
**Solução:** a configuração foi externalizada com variáveis de ambiente.

## 6. Conclusão
A aplicação foi preparada para um fluxo DevOps mais próximo de produção, com padronização de build, reprodutibilidade local e automação de deploy entre ambientes.

## 7. Checklist de Entrega
| Item | OK |
|---|---|
| Projeto compactado em .ZIP com estrutura organizada | ☒ |
| Dockerfile funcional | ☒ |
| docker-compose.yml ou arquivos Kubernetes | ☒ |
| Pipeline com etapas de build, teste e deploy | ☒ |
| README.md com instruções e prints | ☒ |
| Documentação técnica com evidências (PDF ou PPT) | ☒ |
| Deploy realizado nos ambientes staging e produção | ☐ |
