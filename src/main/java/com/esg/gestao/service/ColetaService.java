package com.esg.gestao.service;

import com.esg.gestao.exception.ResourceNotFoundException;
import com.esg.gestao.exception.BusinessException;
import com.esg.gestao.model.dto.ColetaDTO;
import com.esg.gestao.model.entity.Coleta;
import com.esg.gestao.model.entity.Residuo;
import com.esg.gestao.model.entity.Transportador;
import com.esg.gestao.model.enums.StatusColeta;
import com.esg.gestao.repository.ColetaRepository;
import com.esg.gestao.repository.ResiduoRepository;
import com.esg.gestao.repository.TransportadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ColetaService {

    private final ColetaRepository coletaRepository;
    private final ResiduoRepository residuoRepository;
    private final TransportadorRepository transportadorRepository;

    @Transactional(readOnly = true)
    public List<ColetaDTO> findAll() {
        return coletaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ColetaDTO findById(Long id) {
        Coleta coleta = coletaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coleta não encontrada"));
        return convertToDTO(coleta);
    }

    @Transactional(readOnly = true)
    public List<ColetaDTO> findByStatus(StatusColeta status) {
        return coletaRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ColetaDTO> findColetasPendentesAtrasadas() {
        LocalDate dataLimite = LocalDate.now().minusDays(7);
        return coletaRepository.findColetasPendentesAtrasadas(StatusColeta.PENDENTE, dataLimite)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ColetaDTO create(ColetaDTO coletaDTO) {
        // Validar resíduo
        Residuo residuo = residuoRepository.findById(coletaDTO.getIdResiduo())
                .orElseThrow(() -> new ResourceNotFoundException("Resíduo não encontrado"));

        // Validar transportador
        Transportador transportador = transportadorRepository.findById(coletaDTO.getIdTransportador())
                .orElseThrow(() -> new ResourceNotFoundException("Transportador não encontrado"));

        // Validar licença ambiental
        if (transportador.getLicencaAmbiental() == null || transportador.getLicencaAmbiental().isEmpty()) {
            throw new BusinessException("Transportador sem licença ambiental válida");
        }

        Coleta coleta = new Coleta();
        coleta.setDataColeta(coletaDTO.getDataColeta());
        coleta.setStatus(coletaDTO.getStatus());
        coleta.setResiduo(residuo);
        coleta.setTransportador(transportador);

        coleta = coletaRepository.save(coleta);
        return convertToDTO(coleta);
    }

    @Transactional
    public ColetaDTO update(Long id, ColetaDTO coletaDTO) {
        Coleta coleta = coletaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coleta não encontrada"));

        coleta.setDataColeta(coletaDTO.getDataColeta());
        coleta.setStatus(coletaDTO.getStatus());

        if (coletaDTO.getIdResiduo() != null) {
            Residuo residuo = residuoRepository.findById(coletaDTO.getIdResiduo())
                    .orElseThrow(() -> new ResourceNotFoundException("Resíduo não encontrado"));
            coleta.setResiduo(residuo);
        }

        if (coletaDTO.getIdTransportador() != null) {
            Transportador transportador = transportadorRepository.findById(coletaDTO.getIdTransportador())
                    .orElseThrow(() -> new ResourceNotFoundException("Transportador não encontrado"));
            coleta.setTransportador(transportador);
        }

        coleta = coletaRepository.save(coleta);
        return convertToDTO(coleta);
    }

    @Transactional
    public void delete(Long id) {
        if (!coletaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Coleta não encontrada");
        }
        coletaRepository.deleteById(id);
    }

    private ColetaDTO convertToDTO(Coleta coleta) {
        ColetaDTO dto = new ColetaDTO();
        dto.setIdColeta(coleta.getIdColeta());
        dto.setDataColeta(coleta.getDataColeta());
        dto.setStatus(coleta.getStatus());
        dto.setIdResiduo(coleta.getResiduo().getIdResiduo());
        dto.setTipoResiduo(coleta.getResiduo().getTipo());
        dto.setIdTransportador(coleta.getTransportador().getIdTransportador());
        dto.setNomeTransportador(coleta.getTransportador().getNome());
        return dto;
    }
}