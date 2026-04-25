# Quick Start - Gestão de Resíduos API

## Início Rápido (5 minutos)

### Pré-requisitos
- Docker e Docker Compose instalados
- Postman ou Insomnia (opcional)

### Passo 1: Clone e Execute

```bash
# Clone o repositório
git clone <url-do-repositorio>
cd gestao-residuos

# Inicie os containers
docker-compose up -d

# Aguarde ~2 minutos para o Oracle inicializar
docker-compose logs -f api
```

Quando ver `Started GestaoResiduosApplication`, a API está pronta!

### Passo 2: Teste Rápido

```bash
# Health Check
curl http://localhost:8080/api/actuator/health

# Login (copie o token retornado)
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Liste os resíduos (substitua SEU_TOKEN)
curl http://localhost:8080/api/residuos \
  -H "Authorization: Bearer SEU_TOKEN"
```

### Passo 3: Explore no Swagger

Abra no navegador: **http://localhost:8080/api/swagger-ui.html**

1. Clique em **"Authorize"** (cadeado no topo)
2. Faça login primeiro em `/auth/login` para obter o token
3. Cole o token no campo de autorização
4. Teste qualquer endpoint!

---

## Fluxo Completo de Uso

### Autenticação

```bash
POST /api/auth/login
Body: {"username": "admin", "password": "admin123"}
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "admin",
  "role": "ADMIN"
}
```

💡 **Copie o token para usar nos próximos passos!**

---

### Cadastrar um Local

```bash
POST /api/locais
Headers: Authorization: Bearer SEU_TOKEN
Body: {
  "nomeLocal": "Centro de Reciclagem Principal",
  "endereco": "Av. Verde, 100"
}
```

---

### Registrar um Resíduo

```bash
POST /api/residuos
Headers: Authorization: Bearer SEU_TOKEN
Body: {
  "tipo": "Plástico",
  "quantidade": 250.00,
  "dataGeracao": "2024-11-05",
  "idLocal": 1
}
```

---

### Agendar uma Coleta

```bash
POST /api/coletas
Headers: Authorization: Bearer SEU_TOKEN
Body: {
  "dataColeta": "2024-11-10",
  "status": "PENDENTE",
  "idResiduo": 1,
  "idTransportador": 1
}
```

**Importante:** O transportador deve ter licença ambiental!

---

### Atualizar Status da Coleta

```bash
PUT /api/coletas/1
Headers: Authorization: Bearer SEU_TOKEN
Body: {
  "dataColeta": "2024-11-10",
  "status": "EM_TRANSPORTE",
  "idResiduo": 1,
  "idTransportador": 1
}
```

---

### Finalizar Coleta

```bash
PUT /api/coletas/1
Headers: Authorization: Bearer SEU_TOKEN
Body: {
  "dataColeta": "2024-11-10",
  "status": "FINALIZADA",
  "idResiduo": 1,
  "idTransportador": 1
}
```

 **Um destino final é criado automaticamente via trigger!**

---

### Verificar Destino Final

```bash
GET /api/destinos/coleta/1
Headers: Authorization: Bearer SEU_TOKEN
```

---

### Ver Estatísticas

```bash
GET /api/residuos/estatisticas/por-tipo
Headers: Authorization: Bearer SEU_TOKEN
```

---

##  Dados Pré-cadastrados

### Locais (5)
1. Fábrica Central
2. Armazém Norte
3. Escritório Administrativo
4. Posto de Coleta Sul
5. Unidade Reciclagem Leste

### Transportadores (5)
1. EcoTrans Ltda (LIC-1234) 
2. VerdeLog (LIC-9876) 
3. Recarte Transportes ( SEM LICENÇA)
4. SustentaMov (LIC-2222) 
5. TransEco (LIC-3333) 

### Resíduos (5)
- Plástico: 150 kg
- Metal: 200 kg
- Vidro: 120 kg
- Papel: 300 kg
- Orgânico: 500 kg

### Usuários (2)

| Username | Password | Role | Email |
|----------|----------|------|-------|
| admin | admin123 | ADMIN | admin@gestao-residuos.com |
| operador | operador123 | OPERADOR | operador@gestao-residuos.com |

---

## Cenários de Teste

###  Cenário 1: Happy Path - Coleta Completa

1. Login como admin
2. Criar novo resíduo
3. Agendar coleta (com transportador licenciado)
4. Atualizar status para EM_TRANSPORTE
5. Finalizar coleta
6. Verificar destino final criado automaticamente

###  Cenário 2: Validação - Transportador sem Licença

1. Login como admin
2. Tentar agendar coleta com transportador ID 3 (Recarte - sem licença)
3. **Resultado esperado:** Erro 400 - "Transportador sem licença ambiental válida"

###  Cenário 3: Autorização - Usuário sem Permissão

1. Login como usuário normal (criar um via `/auth/register`)
2. Tentar criar um resíduo via POST /api/residuos
3. **Resultado esperado:** Erro 403 - Forbidden

### ⚠ Cenário 4: Alerta - Coleta Atrasada

1. Login como admin ou operador
2. Buscar coletas pendentes atrasadas: GET /api/coletas/pendentes-atrasadas
3. **Resultado esperado:** Lista de coletas pendentes há mais de 7 dias

---

##  Endpoints Mais Usados

### Top 10 Endpoints

1. `POST /auth/login` - Autenticação
2. `GET /residuos` - Listar resíduos
3. `POST /residuos` - Registrar resíduo
4. `POST /coletas` - Agendar coleta
5. `PUT /coletas/{id}` - Atualizar coleta
6. `GET /coletas/status/PENDENTE` - Coletas pendentes
7. `GET /residuos/estatisticas/por-tipo` - Estatísticas
8. `GET /transportadores/com-licenca` - Transportadores válidos
9. `GET /destinos` - Destinos finais
10. `GET /coletas/pendentes-atrasadas` - Alertas

---

##  Troubleshooting Rápido

### Problema: "Connection refused"
```bash
# Verifique se os containers estão rodando
docker-compose ps

# Se não estiverem, inicie
docker-compose up -d

# Veja os logs
docker-compose logs -f
```

### Problema: "Unauthorized" (401)
- Você fez login primeiro?
- O token está correto no header `Authorization: Bearer TOKEN`?
- O token expirou? (Válido por 24h)

### Problema: "Forbidden" (403)
- Verifique se seu usuário tem permissão para a operação
- ADMIN: tudo
- OPERADOR: não pode DELETE
- USUARIO: só GET

### Problema: "Table does not exist"
- As migrations não executaram
- Verifique os logs: `docker-compose logs api`
- Recrie o ambiente: `docker-compose down -v && docker-compose up -d`

### Problema: "Transportador sem licença"
- Use um transportador com licença válida (IDs: 1, 2, 4 ou 5)
- Evite o transportador ID 3 (Recarte - sem licença)

---

##  Testando com Postman

### 1. Importe a Collection

1. Abra o Postman
2. File → Import
3. Selecione `Gestao_Residuos_API.postman_collection.json`
4. A collection será importada com todos os endpoints

### 2. Configure as Variáveis

A collection já vem com variáveis configuradas:
- `baseUrl`: http://localhost:8080/api
- `token`: (será preenchido automaticamente após login)

### 3. Faça o Login

1. Abra a pasta **Autenticação**
2. Execute **Login - Admin**
3. O token será salvo automaticamente na variável `token`

### 4. Teste os Endpoints

Todos os endpoints já estão configurados para usar o token automaticamente!

---

##  Dicas Profissionais

###  Para Desenvolvimento

```bash
# Ver logs em tempo real
docker-compose logs -f api

# Reiniciar apenas a API (após mudanças)
docker-compose restart api

# Parar tudo
docker-compose down

# Limpar tudo (incluindo volumes)
docker-compose down -v
```

###  Para Debug

```bash
# Entrar no container da API
docker-compose exec api sh

# Entrar no Oracle
docker-compose exec oracle-db sqlplus system/oracle@XE

# Ver status dos containers
docker-compose ps

# Ver uso de recursos
docker stats
```

### 🎯 Para Produção

1. Mude as senhas em `application.properties`
2. Mude a secret JWT
3. Configure HTTPS/SSL
4. Use variáveis de ambiente
5. Configure backup automático do BD
6. Adicione rate limiting
7. Configure monitoring (Prometheus + Grafana)

---

##  Próximos Passos

1. ✅ Explore todos os endpoints no Swagger
2. ✅ Teste diferentes roles (admin, operador, usuário)
3. ✅ Crie cenários de teste customizados
4. ✅ Analise os logs e métricas
5. ✅ Experimente os relatórios e estatísticas
6. ✅ Teste os triggers automáticos
7. ✅ Valide as regras de negócio
8. ✅ Teste tratamento de erros

---

##  Precisa de Ajuda?

### Documentação Completa

- **README.md** - Visão geral e instalação
- **ENDPOINTS.md** - Lista completa de endpoints
- **FEATURES_AVANCADAS.md** - Funcionalidades extras
- **INSTRUÇÕES_ENTREGA.md** - Guia de montagem

### Swagger UI

http://localhost:8080/api/swagger-ui.html

### Logs

```bash
docker-compose logs api
```

---

## Checklist de Validação

Antes de entregar, teste:

- [ ] API está rodando (health check)
- [ ] Login funciona com admin
- [ ] Consegue criar um resíduo
- [ ] Consegue agendar uma coleta
- [ ] Consegue atualizar status da coleta
- [ ] Trigger cria destino final automaticamente
- [ ] Transportador sem licença retorna erro
- [ ] Swagger UI está acessível
- [ ] Postman collection funciona
- [ ] Docker compose sobe sem erros

---

 **Pronto! Você tem uma API completa de Gestão de Resíduos funcionando!**

**Desenvolvido com ♻️ para um mundo mais sustentável**