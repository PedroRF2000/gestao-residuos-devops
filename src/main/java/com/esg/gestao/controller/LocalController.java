package com.esg.gestao.controller;

import com.esg.gestao.model.dto.LocalDTO;
import com.esg.gestao.service.LocalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locais")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-token")
@Tag(name = "Locais", description = "Gerenciamento de locais de geração de resíduos")
public class LocalController {

    private final LocalService localService;

    @GetMapping
    @Operation(summary = "Listar locais", description = "Retorna todos os locais cadastrados")
    public ResponseEntity<List<LocalDTO>> findAll() {
        return ResponseEntity.ok(localService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar local por ID", description = "Retorna um local específico")
    public ResponseEntity<LocalDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(localService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Cadastrar local", description = "Cadastra um novo local de geração de resíduos")
    public ResponseEntity<LocalDTO> create(@Valid @RequestBody LocalDTO localDTO) {
        LocalDTO created = localService.create(localDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar local", description = "Atualiza dados de um local")
    public ResponseEntity<LocalDTO> update(@PathVariable Long id, @Valid @RequestBody LocalDTO localDTO) {
        return ResponseEntity.ok(localService.update(id, localDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover local", description = "Remove um local do sistema")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        localService.delete(id);
        return ResponseEntity.noContent().build();
    }
}