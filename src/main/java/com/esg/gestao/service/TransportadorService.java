package com.esg.gestao.service;

import com.esg.gestao.exception.ResourceNotFoundException;
import com.esg.gestao.model.dto.TransportadorDTO;
import com.esg.gestao.model.entity.Transportador;
import com.esg.gestao.repository.TransportadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransportadorService {

    private final TransportadorRepository transportadorRepository;

    @Transactional(readOnly = true)
    public List<TransportadorDTO> findAll() {
        return transportadorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TransportadorDTO findById(Long id) {
        Transportador transportador = transportadorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transportador não encontrado"));
        return convertToDTO(transportador);
    }

    @Transactional(readOnly = true)
    public List<TransportadorDTO> findComLicenca() {
        return transportadorRepository.findByLicencaAmbientalIsNotNull().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public TransportadorDTO create(TransportadorDTO transportadorDTO) {
        // Verificar se CNPJ já existe
        if (transportadorRepository.findByCnpj(transportadorDTO.getCnpj()).isPresent()) {
            throw new IllegalArgumentException("CNPJ já cadastrado");
        }

        Transportador transportador = new Transportador();
        transportador.setNome(transportadorDTO.getNome());
        transportador.setCnpj(transportadorDTO.getCnpj());
        transportador.setLicencaAmbiental(transportadorDTO.getLicencaAmbiental());

        transportador = transportadorRepository.save(transportador);
        return convertToDTO(transportador);
    }

    @Transactional
    public TransportadorDTO update(Long id, TransportadorDTO transportadorDTO) {
        Transportador transportador = transportadorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transportador não encontrado"));

        // Verificar se está tentando alterar CNPJ para um já existente
        if (!transportador.getCnpj().equals(transportadorDTO.getCnpj())) {
            if (transportadorRepository.findByCnpj(transportadorDTO.getCnpj()).isPresent()) {
                throw new IllegalArgumentException("CNPJ já cadastrado");
            }
        }

        transportador.setNome(transportadorDTO.getNome());
        transportador.setCnpj(transportadorDTO.getCnpj());
        transportador.setLicencaAmbiental(transportadorDTO.getLicencaAmbiental());

        transportador = transportadorRepository.save(transportador);
        return convertToDTO(transportador);
    }

    @Transactional
    public void delete(Long id) {
        if (!transportadorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Transportador não encontrado");
        }
        transportadorRepository.deleteById(id);
    }

    private TransportadorDTO convertToDTO(Transportador transportador) {
        TransportadorDTO dto = new TransportadorDTO();
        dto.setIdTransportador(transportador.getIdTransportador());
        dto.setNome(transportador.getNome());
        dto.setCnpj(transportador.getCnpj());
        dto.setLicencaAmbiental(transportador.getLicencaAmbiental());
        return dto;
    }
}