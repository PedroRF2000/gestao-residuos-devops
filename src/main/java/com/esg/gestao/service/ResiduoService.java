package com.esg.gestao.service;

import com.esg.gestao.exception.ResourceNotFoundException;
import com.esg.gestao.model.dto.ResiduoDTO;
import com.esg.gestao.model.entity.Local;
import com.esg.gestao.model.entity.Residuo;
import com.esg.gestao.repository.LocalRepository;
import com.esg.gestao.repository.ResiduoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResiduoService {

    private final ResiduoRepository residuoRepository;
    private final LocalRepository localRepository;

    @Transactional(readOnly = true)
    public List<ResiduoDTO> findAll() {
        return residuoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ResiduoDTO findById(Long id) {
        Residuo residuo = residuoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resíduo não encontrado"));
        return convertToDTO(residuo);
    }

    @Transactional(readOnly = true)
    public List<ResiduoDTO> findByTipo(String tipo) {
        return residuoRepository.findByTipo(tipo).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ResiduoDTO> findByLocalId(Long idLocal) {
        return residuoRepository.findByLocalIdLocal(idLocal).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Map<String, BigDecimal> getQuantidadePorTipo() {
        List<Object[]> results = residuoRepository.findQuantidadePorTipo();
        return results.stream()
                .collect(Collectors.toMap(
                        r -> (String) r[0],
                        r -> (BigDecimal) r[1]
                ));
    }

    @Transactional
    public ResiduoDTO create(ResiduoDTO residuoDTO) {
        Local local = localRepository.findById(residuoDTO.getIdLocal())
                .orElseThrow(() -> new ResourceNotFoundException("Local não encontrado"));

        Residuo residuo = new Residuo();
        residuo.setTipo(residuoDTO.getTipo());
        residuo.setQuantidade(residuoDTO.getQuantidade());
        residuo.setDataGeracao(residuoDTO.getDataGeracao());
        residuo.setLocal(local);

        residuo = residuoRepository.save(residuo);
        return convertToDTO(residuo);
    }

    @Transactional
    public ResiduoDTO update(Long id, ResiduoDTO residuoDTO) {
        Residuo residuo = residuoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resíduo não encontrado"));

        residuo.setTipo(residuoDTO.getTipo());
        residuo.setQuantidade(residuoDTO.getQuantidade());
        residuo.setDataGeracao(residuoDTO.getDataGeracao());

        if (residuoDTO.getIdLocal() != null) {
            Local local = localRepository.findById(residuoDTO.getIdLocal())
                    .orElseThrow(() -> new ResourceNotFoundException("Local não encontrado"));
            residuo.setLocal(local);
        }

        residuo = residuoRepository.save(residuo);
        return convertToDTO(residuo);
    }

    @Transactional
    public void delete(Long id) {
        if (!residuoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resíduo não encontrado");
        }
        residuoRepository.deleteById(id);
    }

    private ResiduoDTO convertToDTO(Residuo residuo) {
        ResiduoDTO dto = new ResiduoDTO();
        dto.setIdResiduo(residuo.getIdResiduo());
        dto.setTipo(residuo.getTipo());
        dto.setQuantidade(residuo.getQuantidade());
        dto.setDataGeracao(residuo.getDataGeracao());
        dto.setIdLocal(residuo.getLocal().getIdLocal());
        dto.setNomeLocal(residuo.getLocal().getNomeLocal());
        return dto;
    }
}