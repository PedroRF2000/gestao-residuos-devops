# Endpoints da API - Gestão de Resíduos

##  Base URL
```
http://localhost:8080/api
```

##  Endpoints Públicos (Sem Autenticação)

### Autenticação

| Método | Endpoint | Descrição | Body |
|--------|----------|-----------|------|
| POST | `/auth/login` | Realiza login e retorna JWT | `{"username": "admin", "password": "admin123"}` |
| POST | `/auth/register` | Registra novo usuário | `{"username": "user", "password": "pass", "email": "email@test.com", "nomeCompleto": "Nome", "role": "USUARIO"}` |

### Documentação

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/swagger-ui.html` | Interface Swagger UI |
| GET | `/api-docs` | Documentação OpenAPI JSON |

### Health Check

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/actuator/health` | Status da aplicação |

---

##  Endpoints Protegidos (Requer JWT)

> **Nota:** Adicione o header `Authorization: Bearer {seu-token}` em todas as requisições

---

## 🗑 Resíduos (`/residuos`)

### Listar e Consultar

| Método | Endpoint | Descrição | Permissões |
|--------|----------|-----------|------------|
| GET | `/residuos` | Lista todos os resíduos | ADMIN, OPERADOR, USUARIO |
| GET | `/residuos/{id}` | Busca resíduo por ID | ADMIN, OPERADOR, USUARIO |
| GET | `/residuos/tipo/{tipo}` | Filtra resíduos por tipo | ADMIN, OPERADOR, USUARIO |
| GET | `/residuos/local/{idLocal}` | Filtra resíduos por local | ADMIN, OPERADOR, USUARIO |
| GET | `/residuos/estatisticas/por-tipo` | Estatísticas agrupadas por tipo | ADMIN, OPERADOR, USUARIO |

**Tipos de Resíduo:** Plástico, Metal, Vidro, Papel, Orgânico, Eletrônico

### Gerenciar

| Método | Endpoint | Descrição | Body | Permissões |
|--------|----------|-----------|------|------------|
| POST | `/residuos` | Registra novo resíduo | Ver exemplo abaixo | ADMIN, OPERADOR |
| PUT | `/residuos/{id}` | Atualiza resíduo | Ver exemplo abaixo | ADMIN, OPERADOR |
| DELETE | `/residuos/{id}` | Remove resíduo | - | ADMIN |

**Body POST/PUT:**
```json
{
  "tipo": "Plástico",
  "quantidade": 150.50,
  "dataGeracao": "2024-11-05",
  "idLocal": 1
}
```

---

##  Coletas (`/coletas`)

### Listar e Consultar

| Método | Endpoint | Descrição | Permissões |
|--------|----------|-----------|------------|
| GET | `/coletas` | Lista todas as coletas | ADMIN, OPERADOR, USUARIO |
| GET | `/coletas/{id}` | Busca coleta por ID | ADMIN, OPERADOR, USUARIO |
| GET | `/coletas/status/{status}` | Filtra coletas por status | ADMIN, OPERADOR, USUARIO |
| GET | `/coletas/pendentes-atrasadas` | Coletas pendentes há +7 dias | ADMIN, OPERADOR, USUARIO |

**Status de Coleta:** PENDENTE, EM_TRANSPORTE, FINALIZADA, CANCELADA

### Gerenciar

| Método | Endpoint | Descrição | Body | Permissões |
|--------|----------|-----------|------|------------|
| POST | `/coletas` | Agenda nova coleta | Ver exemplo abaixo | ADMIN, OPERADOR |
| PUT | `/coletas/{id}` | Atualiza/muda status da coleta | Ver exemplo abaixo | ADMIN, OPERADOR |
| DELETE | `/coletas/{id}` | Cancela coleta | - | ADMIN |

**Body POST:**
```json
{
  "dataColeta": "2024-11-10",
  "status": "PENDENTE",
  "idResiduo": 1,
  "idTransportador": 1
}
```

**Body PUT (Atualizar Status):**
```json
{
  "dataColeta": "2024-11-10",
  "status": "EM_TRANSPORTE",
  "idResiduo": 1,
  "idTransportador": 1
}
```

**Body PUT (Finalizar):**
```json
{
  "dataColeta": "2024-11-10",
  "status": "FINALIZADA",
  "idResiduo": 1,
  "idTransportador": 1
}
```

> **Nota:** Ao finalizar uma coleta (status = FINALIZADA), um destino final é criado automaticamente via trigger do banco de dados.

---

##  Locais (`/locais`)

### Listar e Consultar

| Método | Endpoint | Descrição | Permissões |
|--------|----------|-----------|------------|
| GET | `/locais` | Lista todos os locais | ADMIN, OPERADOR, USUARIO |
| GET | `/locais/{id}` | Busca local por ID | ADMIN, OPERADOR, USUARIO |

### Gerenciar

| Método | Endpoint | Descrição | Body | Permissões |
|--------|----------|-----------|------|------------|
| POST | `/locais` | Cadastra novo local | Ver exemplo abaixo | ADMIN, OPERADOR |
| PUT | `/locais/{id}` | Atualiza local | Ver exemplo abaixo | ADMIN, OPERADOR |
| DELETE | `/locais/{id}` | Remove local | - | ADMIN |

**Body POST/PUT:**
```json
{
  "nomeLocal": "Centro de Distribuição Oeste",
  "endereco": "Av. Oeste, 1200"
}
```

---

##  Transportadores (`/transportadores`)

### Listar e Consultar

| Método | Endpoint | Descrição | Permissões |
|--------|----------|-----------|------------|
| GET | `/transportadores` | Lista todos os transportadores | ADMIN, OPERADOR, USUARIO |
| GET | `/transportadores/{id}` | Busca transportador por ID | ADMIN, OPERADOR, USUARIO |
| GET | `/transportadores/com-licenca` | Lista apenas transportadores com licença válida | ADMIN, OPERADOR, USUARIO |

### Gerenciar

| Método | Endpoint | Descrição | Body | Permissões |
|--------|----------|-----------|------|------------|
| POST | `/transportadores` | Cadastra novo transportador | Ver exemplo abaixo | ADMIN, OPERADOR |
| PUT | `/transportadores/{id}` | Atualiza transportador | Ver exemplo abaixo | ADMIN, OPERADOR |
| DELETE | `/transportadores/{id}` | Remove transportador | - | ADMIN |

**Body POST/PUT:**
```json
{
  "nome": "EcoTrans Ltda",
  "cnpj": "12345678000101",
  "licencaAmbiental": "LIC-1234"
}
```

> **️ Validação Importante:** Ao agendar uma coleta, o transportador DEVE ter uma licença ambiental válida. Caso contrário, o sistema retorna erro 400.

---

##  Destinos Finais (`/destinos`)

### Listar e Consultar

| Método | Endpoint | Descrição | Permissões |
|--------|----------|-----------|------------|
| GET | `/destinos` | Lista todos os destinos finais | ADMIN, OPERADOR, USUARIO |
| GET | `/destinos/{id}` | Busca destino por ID | ADMIN, OPERADOR, USUARIO |
| GET | `/destinos/coleta/{idColeta}` | Busca destinos de uma coleta | ADMIN, OPERADOR, USUARIO |
| GET | `/destinos/tipo/{tipo}` | Filtra destinos por tipo | ADMIN, OPERADOR, USUARIO |
| GET | `/destinos/estatisticas/por-tipo` | Estatísticas por tipo de destino | ADMIN, OPERADOR, USUARIO |

### Gerenciar

| Método | Endpoint | Descrição | Body | Permissões |
|--------|----------|-----------|------|------------|
| POST | `/destinos` | Registra novo destino final | Ver exemplo abaixo | ADMIN, OPERADOR |
| PUT | `/destinos/{id}` | Atualiza destino | Ver exemplo abaixo | ADMIN, OPERADOR |
| DELETE | `/destinos/{id}` | Remove destino | - | ADMIN |

**Body POST/PUT:**
```json
{
  "tipoDestino": "Reciclagem",
  "localDestino": "Centro Municipal de Reciclagem",
  "dataRecebimento": "2024-11-05",
  "idColeta": 1
}
```

---

##  Alertas (`/alertas`)

### Listar e Consultar

| Método | Endpoint | Descrição | Permissões |
|--------|----------|-----------|------------|
| GET | `/alertas` | Lista todos os alertas | ADMIN, OPERADOR |
| GET | `/alertas/{id}` | Busca alerta por ID | ADMIN, OPERADOR |
| GET | `/alertas/periodo` | Filtra alertas por período | ADMIN, OPERADOR |
| GET | `/alertas/recentes` | Últimos 10 alertas | ADMIN, OPERADOR |

---

##  Relatórios (`/relatorios`)

### Relatórios Disponíveis

| Método | Endpoint | Descrição | Query Params | Permissões |
|--------|----------|-----------|--------------|------------|
| GET | `/relatorios/coletas-periodo` | Relatório de coletas por período | `inicio`, `fim` | ADMIN, OPERADOR, USUARIO |
| GET | `/relatorios/residuos-tipo` | Resumo de resíduos por tipo | - | ADMIN, OPERADOR, USUARIO |
| GET | `/relatorios/coletas-status` | Contagem de coletas por status | - | ADMIN, OPERADOR, USUARIO |
| GET | `/relatorios/transportadores-desempenho` | Desempenho dos transportadores | - | ADMIN, OPERADOR |
| GET | `/relatorios/locais-geracao` | Maiores geradores de resíduos | - | ADMIN, OPERADOR, USUARIO |

**Exemplo com Query Params:**
```
GET /relatorios/coletas-periodo?inicio=2024-01-01&fim=2024-12-31
```

---

##  Estatísticas e Dashboards

### Endpoints de Métricas

| Método | Endpoint | Descrição | Permissões |
|--------|----------|-----------|------------|
| GET | `/dashboard/resumo` | Resumo geral do sistema | ADMIN, OPERADOR |
| GET | `/dashboard/coletas-mes` | Total de coletas no mês | ADMIN, OPERADOR |
| GET | `/dashboard/residuos-total` | Total de resíduos por tipo | ADMIN, OPERADOR, USUARIO |
| GET | `/dashboard/meta-reciclagem` | Percentual de resíduos reciclados | ADMIN, OPERADOR, USUARIO |

---

## Resumo de Endpoints

### Total de Endpoints: **40+**

| Categoria | Quantidade | Descrição |
|-----------|------------|-----------|
| Autenticação | 2 | Login e registro |
| Resíduos | 8 | CRUD completo + filtros + estatísticas |
| Coletas | 8 | CRUD completo + filtros + alertas |
| Locais | 5 | CRUD completo |
| Transportadores | 6 | CRUD completo + validações |
| Destinos | 8 | CRUD completo + filtros + estatísticas |
| Alertas | 4 | Listagem e consultas |
| Relatórios | 5 | Diversos relatórios |
| Dashboard | 4 | Métricas e estatísticas |
| Documentação | 3 | Swagger e health |

---

##  Matriz de Permissões

| Operação | ADMIN | OPERADOR | USUARIO |
|----------|-------|----------|---------|
| **GET** (Listar/Consultar) | ✅ | ✅ | ✅ |
| **POST** (Criar) | ✅ | ✅ | ❌ |
| **PUT** (Atualizar) | ✅ | ✅ | ❌ |
| **DELETE** (Remover) | ✅ | ❌ | ❌ |
| **Alertas** | ✅ | ✅ | ❌ |
| **Relatórios Completos** | ✅ | ✅ | Limitado |
| **Dashboard Administrativo** | ✅ | ✅ | ❌ |

---

##  Testando os Endpoints

### 1. Fazer Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

### 2. Usar o Token
```bash
curl -X GET http://localhost:8080/api/residuos \
  -H "Authorization: Bearer {seu-token-aqui}"
```

### 3. Criar um Resíduo
```bash
curl -X POST http://localhost:8080/api/residuos \
  -H "Authorization: Bearer {seu-token-aqui}" \
  -H "Content-Type: application/json" \
  -d '{
    "tipo": "Plástico",
    "quantidade": 100.00,
    "dataGeracao": "2024-11-05",
    "idLocal": 1
  }'
```

### 4. Agendar uma Coleta
```bash
curl -X POST http://localhost:8080/api/coletas \
  -H "Authorization: Bearer {seu-token-aqui}" \
  -H "Content-Type: application/json" \
  -d '{
    "dataColeta": "2024-11-10",
    "status": "PENDENTE",
    "idResiduo": 1,
    "idTransportador": 1
  }'
```

---

##  Endpoints Obrigatórios Implementados

✅ **GET /residuos** - Listar resíduos  
✅ **POST /coletas** - Agendar coleta  
✅ **PUT /coletas/{id}** - Atualizar coleta  
✅ **DELETE /coletas/{id}** - Cancelar coleta  
✅ **GET /transportadores/com-licenca** - Listar transportadores habilitados  
✅ **POST /destinos** - Registrar destino final  
✅ **GET /alertas/recentes** - Consultar alertas  
✅ **GET /relatorios/coletas-periodo** - Relatório de coletas

**E mais de 30 endpoints adicionais!**

---

##  Documentação Interativa

Acesse o Swagger UI para testar todos os endpoints de forma interativa:

```
http://localhost:8080/api/swagger-ui.html
```

A documentação inclui:
- Descrição detalhada de cada endpoint
- Exemplos de request/response
- Possibilidade de testar direto no navegador
- Schemas dos DTOs
- Códigos de status HTTP

---

**Desenvolvido com para um mundo mais sustentável**