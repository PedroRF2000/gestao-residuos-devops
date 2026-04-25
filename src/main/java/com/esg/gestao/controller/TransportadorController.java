package com.esg.gestao.controller;

import com.esg.gestao.model.dto.TransportadorDTO;
import com.esg.gestao.service.TransportadorService;
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
@RequestMapping("/transportadores")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-token")
@Tag(name = "Transportadores", description = "Gerenciamento de transportadores de resíduos")
public class TransportadorController {

    private final TransportadorService transportadorService;

    @GetMapping
    @Operation(summary = "Listar transportadores", description = "Retorna todos os transportadores cadastrados")
    public ResponseEntity<List<TransportadorDTO>> findAll() {
        return ResponseEntity.ok(transportadorService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar transportador por ID", description = "Retorna um transportador específico")
    public ResponseEntity<TransportadorDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(transportadorService.findById(id));
    }

    @GetMapping("/com-licenca")
    @Operation(summary = "Listar transportadores com licença",
            description = "Retorna apenas transportadores com licença ambiental válida")
    public ResponseEntity<List<TransportadorDTO>> findComLicenca() {
        return ResponseEntity.ok(transportadorService.findComLicenca());
    }

    @PostMapping
    @Operation(summary = "Cadastrar transportador", description = "Cadastra um novo transportador")
    public ResponseEntity<TransportadorDTO> create(@Valid @RequestBody TransportadorDTO transportadorDTO) {
        TransportadorDTO created = transportadorService.create(transportadorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar transportador", description = "Atualiza dados de um transportador")
    public ResponseEntity<TransportadorDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody TransportadorDTO transportadorDTO) {
        return ResponseEntity.ok(transportadorService.update(id, transportadorDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover transportador", description = "Remove um transportador do sistema")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transportadorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}