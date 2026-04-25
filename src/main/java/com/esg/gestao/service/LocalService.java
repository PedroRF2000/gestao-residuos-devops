package com.esg.gestao.service;

import com.esg.gestao.exception.ResourceNotFoundException;
import com.esg.gestao.model.dto.LocalDTO;
import com.esg.gestao.model.entity.Local;
import com.esg.gestao.repository.LocalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocalService {

    private final LocalRepository localRepository;

    @Transactional(readOnly = true)
    public List<LocalDTO> findAll() {
        return localRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LocalDTO findById(Long id) {
        Local local = localRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Local não encontrado"));
        return convertToDTO(local);
    }

    @Transactional
    public LocalDTO create(LocalDTO localDTO) {
        Local local = new Local();
        local.setNomeLocal(localDTO.getNomeLocal());
        local.setEndereco(localDTO.getEndereco());

        local = localRepository.save(local);
        return convertToDTO(local);
    }

    @Transactional
    public LocalDTO update(Long id, LocalDTO localDTO) {
        Local local = localRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Local não encontrado"));

        local.setNomeLocal(localDTO.getNomeLocal());
        local.setEndereco(localDTO.getEndereco());

        local = localRepository.save(local);
        return convertToDTO(local);
    }

    @Transactional
    public void delete(Long id) {
        if (!localRepository.existsById(id)) {
            throw new ResourceNotFoundException("Local não encontrado");
        }
        localRepository.deleteById(id);
    }

    private LocalDTO convertToDTO(Local local) {
        LocalDTO dto = new LocalDTO();
        dto.setIdLocal(local.getIdLocal());
        dto.setNomeLocal(local.getNomeLocal());
        dto.setEndereco(local.getEndereco());
        return dto;
    }
}