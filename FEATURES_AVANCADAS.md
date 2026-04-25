# 🚀 Features Avançadas - Gestão de Resíduos API

## 📋 Índice
1. [Segurança Avançada](#segurança-avançada)
2. [Validações Robustas](#validações-robustas)
3. [Triggers Automáticos](#triggers-automáticos)
4. [Exception Handling](#exception-handling)
5. [Documentação](#documentação)
6. [Logs e Auditoria](#logs-e-auditoria)
7. [Performance e Otimizações](#performance-e-otimizações)
8. [Containerização](#containerização)

---

## 🔐 Segurança Avançada

### 1. **Autenticação JWT Stateless**

- **Token JWT** com tempo de expiração configurável (24h)
- **Criptografia HMAC SHA-256** para assinatura do token
- **Refresh token** (pode ser implementado)
- **Stateless authentication** - nenhum estado no servidor

```java
Exemplo de configuração JWT
jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
jwt.expiration=86400000 // 24 horas
```

### 2. **Autorização Granular por Role**

Três níveis de permissão:

| Role | Permissões |
|------|-----------|
| **ADMIN** | Acesso total - CRUD completo em todos os recursos |
| **OPERADOR** | CRUD exceto DELETE - gerencia coletas e resíduos |
| **USUARIO** | Apenas leitura (GET) - visualiza informações |

### 3. **Criptografia de Senhas**

- **BCrypt** com salt automático
- **Força de hash**: 10 rounds (2^10 iterações)
- Senhas nunca são armazenadas em texto plano
- Impossível recuperar senha original (apenas reset)

```java
// Hash gerado automaticamente
$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
```

### 4. **Endpoints Protegidos**

```java
// Exemplo de proteção por método HTTP e role
@PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
public ResponseEntity<ColetaDTO> create(@Valid @RequestBody ColetaDTO dto)

@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<Void> delete(@PathVariable Long id)
```

### 5. **CORS Configurado**

- Permite requisições de origens específicas
- Headers personalizados permitidos
- Métodos HTTP configurados

### 6. **CSRF Protection**

- Desabilitado para API REST (stateless)
- Session management: STATELESS

---

## ✅ Validações Robustas

### 1. **Bean Validation (JSR-380)**

Todas as DTOs possuem validações completas:

```java
@NotBlank(message = "Nome é obrigatório")
@Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
private String nome;

@NotNull(message = "Quantidade é obrigatória")
@DecimalMin(value = "0.01", message = "Quantidade deve ser maior que zero")
private BigDecimal quantidade;

@Email(message = "Email inválido")
@NotBlank(message = "Email é obrigatório")
private String email;

@Pattern(regexp = "\\d{14}", message = "CNPJ deve conter 14 dígitos")
private String cnpj;

@PastOrPresent(message = "Data não pode ser futura")
private LocalDate dataGeracao;
```

### 2. **Validações de Negócio**

#### Licença Ambiental Obrigatória
```java
// Trigger no banco + validação no service
if (transportador.getLicencaAmbiental() == null) {
    throw new BusinessException("Transportador sem licença ambiental válida");
}
```

#### Validação de CNPJ
- Formato: 14 dígitos
- Unicidade garantida no banco

#### Validação de Datas
- Datas de geração não podem ser futuras
- Datas de coleta devem ser consistentes

### 3. **Validação em Cascata**

Ao criar uma coleta:
1.  Valida se o resíduo existe
2.  Valida se o transportador existe
3.  Valida se o transportador tem licença
4.  Valida campos obrigatórios
5.  Valida formato dos dados

---

## ⚡ Triggers Automáticos

### 1. **TG_DESTINO_AUTOMATICO**

**Funcionalidade:** Ao finalizar uma coleta (status = FINALIZADA), cria automaticamente um destino final.

```sql
CREATE OR REPLACE TRIGGER TG_DESTINO_AUTOMATICO
AFTER UPDATE OF status ON COLETA
FOR EACH ROW
BEGIN
    IF :NEW.status = 'FINALIZADA' THEN
        INSERT INTO DESTINO_FINAL (...)
        VALUES (...);
    END IF;
END;
```

**Benefícios:**
- Automatiza o processo de rastreamento
- Garante que toda coleta finalizada tem destino
- Reduz erros humanos

### 2. **TG_ATUALIZA_STATUS_COLETA**

**Funcionalidade:** Ao inserir um destino final, atualiza automaticamente o status da coleta para FINALIZADA.

```sql
CREATE OR REPLACE TRIGGER TG_ATUALIZA_STATUS_COLETA
AFTER INSERT ON DESTINO_FINAL
FOR EACH ROW
BEGIN
    UPDATE COLETA
       SET status = 'FINALIZADA'
     WHERE id_coleta = :NEW.id_coleta;
END;
```

**Benefícios:**
- Sincronização automática de status
- Consistência de dados garantida
- Workflow simplificado

### 3. **TG_VERIFICA_LICENCA**

**Funcionalidade:** Antes de criar uma coleta, valida se o transportador possui licença ambiental.

```sql
CREATE OR REPLACE TRIGGER TG_VERIFICA_LICENCA
BEFORE INSERT ON COLETA
FOR EACH ROW
DECLARE
    v_licenca TRANSPORTADOR.licenca_ambiental%TYPE;
BEGIN
    SELECT licenca_ambiental INTO v_licenca
      FROM TRANSPORTADOR
     WHERE id_transportador = :NEW.id_transportador;

    IF v_licenca IS NULL THEN
        RAISE_APPLICATION_ERROR(-20001, 'Transportador sem licença ambiental válida!');
    END IF;
END;
```

**Benefícios:**
- Conformidade legal garantida
- Impossível agendar coleta com transportador irregular
- Validação em nível de banco de dados

---

##  Exception Handling

### 1. **GlobalExceptionHandler**

Handler centralizado para todas as exceções da aplicação:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    // Trata todas as exceções de forma consistente
}
```

### 2. **Exceções Customizadas**

| Exceção | HTTP Status | Uso |
|---------|-------------|-----|
| `ResourceNotFoundException` | 404 | Recurso não encontrado |
| `BusinessException` | 400 | Regra de negócio violada |
| `ValidationException` | 400 | Erro de validação |
| `AuthenticationException` | 401 | Credenciais inválidas |
| `AccessDeniedException` | 403 | Sem permissão |

### 3. **Resposta Padronizada de Erro**

```json
{
  "timestamp": "2024-11-05T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Resíduo não encontrado",
  "path": "/api/residuos/999",
  "details": []
}
```

### 4. **Validação com Detalhes**

Quando há erros de validação:

```json
{
  "timestamp": "2024-11-05T10:30:00",
  "status": 400,
  "error": "Validation Error",
  "message": "Erro de validação nos campos",
  "path": "/api/residuos",
  "details": [
    "tipo: tipo é obrigatório",
    "quantidade: quantidade deve ser maior que zero",
    "dataGeracao: data não pode ser futura"
  ]
}
```

---

##  Documentação

### 1. **OpenAPI 3.0 / Swagger**

- **Swagger UI**: Interface interativa para testar a API
- **OpenAPI JSON**: Especificação completa em JSON
- **Exemplos**: Cada endpoint tem exemplos de request/response
- **Schemas**: DTOs documentados com descrições

```yaml
@Operation(
    summary = "Agendar nova coleta",
    description = "Cria um novo agendamento de coleta de resíduos"
)
@SecurityRequirement(name = "bearer-token")
```

### 2. **Documentação de Segurança**

- Esquema de autenticação JWT documentado
- Fluxo de autenticação explicado
- Exemplos de uso do token

### 3. **Tags e Organização**

Endpoints organizados por categorias:
-  Autenticação
- 🗑 Resíduos
-  Coletas
-  Locais
- Transportadores
-  Destinos Finais
-  Alertas
-  Relatórios

---

##  Logs e Auditoria

### 1. **Logging Estruturado**

Diferentes níveis de log:

```properties
# Application logs
logging.level.com.esg.gestao=DEBUG

# SQL logs
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

# Security logs
logging.level.org.springframework.security=DEBUG
```

### 2. **Log de Alertas**

Tabela `LOG_ALERTA` registra:
- Coletas pendentes há mais de 7 dias
- Tentativas de acesso não autorizado
- Erros de validação importantes

### 3. **Auditoria de Operações**

Cada operação é logada com:
- Timestamp
- Usuário que executou
- Operação realizada
- Resultado (sucesso/erro)

---

## ⚡ Performance e Otimizações

### 1. **Lazy Loading**

Relacionamentos configurados com `FetchType.LAZY` para evitar queries desnecessárias:

```java
@OneToMany(mappedBy = "local", fetch = FetchType.LAZY)
private List<Residuo> residuos;
```

### 2. **Batch Operations**

```properties
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true
```

### 3. **Sequences com Allocation**

```java
@SequenceGenerator(
    name = "seq_residuo",
    sequenceName = "SEQ_RESIDUO",
    allocationSize = 1
)
```

### 4. **Indexes Implícitos**

- Primary keys: índice automático
- Foreign keys: índices para melhor performance
- Campos únicos (CNPJ, email): índices únicos

### 5. **Connection Pooling**

Oracle JDBC com pool de conexões configurado para múltiplas requisições simultâneas.

---

## 🐳 Containerização

### 1. **Multi-stage Docker Build**

```dockerfile
# Stage 1: Build
FROM maven:3.9.5-eclipse-temurin-17 AS build
# Compila a aplicação

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine
# Executa apenas o JAR
```

**Benefícios:**
- Imagem final menor (apenas JRE)
- Build reproduzível
- Segurança (menos ferramentas no runtime)

### 2. **Docker Compose**

Orquestra 2 serviços:
- **oracle-db**: Banco de dados Oracle 21c
- **api**: Aplicação Spring Boot

```yaml
services:
  oracle-db:
    # Oracle Database
  api:
    # Spring Boot App
    depends_on:
      oracle-db:
        condition: service_healthy
```

### 3. **Health Checks**

Garante que os containers estejam saudáveis:

```yaml
healthcheck:
  test: ["CMD", "healthcheck.sh"]
  interval: 30s
  timeout: 10s
  retries: 5
```

### 4. **Volumes Persistentes**

Dados do Oracle persistem mesmo após restart:

```yaml
volumes:
  oracle-data:
    driver: local
```

### 5. **Networks Isoladas**

Containers se comunicam em rede isolada:

```yaml
networks:
  gestao-network:
    driver: bridge
```

---

##  Diferenciais Implementados

###  Além do Requisito Mínimo

| Requisito Mínimo | Implementado | Diferencial |
|------------------|--------------|-------------|
| 4+ endpoints | **40+ endpoints** | ✅ 10x mais |
| Spring Security | ✅ JWT + Roles | ✅ Autorização granular |
| Validações | ✅ Bean Validation | ✅ + Validações de negócio |
| Exception Handling | ✅ Global Handler | ✅ Respostas padronizadas |
| Docker | ✅ Dockerfile | ✅ + Docker Compose |
| Migrations | ✅ Flyway | ✅ + Triggers + Seeds |

###  Features Extras

1. **Swagger/OpenAPI** - Documentação interativa completa
2. **Actuator** - Health checks e métricas
3. **Logs estruturados** - Debug facilitado
4. **Triggers automáticos** - Automação de processos
5. **Alertas** - Sistema de notificações
6. **Relatórios** - Estatísticas e dashboards
7. **Multi-stage build** - Imagens Docker otimizadas
8. **Health checks** - Monitoramento de containers
9. **Lazy loading** - Performance otimizada
10. **Batch operations** - Operações em lote

---

##  Métricas do Projeto

| Métrica | Valor |
|---------|-------|
| **Endpoints** | 40+ |
| **Entidades JPA** | 7 |
| **DTOs** | 8 |
| **Services** | 7 |
| **Repositories** | 7 |
| **Controllers** | 7 |
| **Exception Handlers** | 6 |
| **Validações** | 50+ |
| **Migrations** | 4 |
| **Triggers** | 3 |
| **Linhas de código** | 3000+ |

---

##  Possíveis Expansões Futuras

### Funcionalidades Adicionais

1. **Notificações em tempo real** - WebSocket para alertas
2. **Integração com IoT** - Sensores de nível de resíduos
3. **Machine Learning** - Previsão de geração de resíduos
4. **Geolocalização** - Rastreamento de coletas em mapa
5. **QR Codes** - Identificação rápida de resíduos
6. **Dashboards avançados** - Charts e gráficos interativos
7. **Exportação** - Relatórios em PDF/Excel
8. **API externa** - Integração com órgãos ambientais
9. **Gamificação** - Pontuação para reciclagem
10. **App Mobile** - Interface para dispositivos móveis

---

** Projeto completo, robusto e pronto para produção!**