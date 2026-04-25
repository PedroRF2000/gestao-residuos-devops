package com.esg.gestao.controller;

import com.esg.gestao.model.dto.LoginDTO;
import com.esg.gestao.model.dto.TokenDTO;
import com.esg.gestao.model.dto.UsuarioDTO;
import com.esg.gestao.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints de autenticação e registro")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Realiza login e retorna um token JWT")
    public ResponseEntity<TokenDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(authService.login(loginDTO));
    }

    @PostMapping("/register")
    @Operation(summary = "Registro", description = "Registra um novo usuário")
    public ResponseEntity<UsuarioDTO> register(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(authService.register(usuarioDTO));
    }
}