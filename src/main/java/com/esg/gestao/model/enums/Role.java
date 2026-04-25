package com.esg.gestao.model.enums;

/**
 * Enum que define os papéis/roles dos usuários no sistema
 * ADMIN: Acesso total - CRUD completo
 * OPERADOR: Criar, ler e atualizar - não pode deletar
 * USUARIO: Apenas leitura (GET)
 */
public enum Role {
    ADMIN,
    OPERADOR,
    USUARIO
}