package com.esg.gestao.service;

import com.esg.gestao.exception.ResourceNotFoundException;
import com.esg.gestao.model.dto.DestinoFinalDTO;
import com.esg.gestao.model.entity.Coleta;
import com.esg.gestao.model.entity.DestinoFinal;
import com.esg.gestao.repository.ColetaRepository;
import com.esg.gestao.repository.DestinoFinalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DestinoFinalService {

    private final DestinoFinalRepository destinoFinalRepository;
    private final ColetaRepository coletaRepository;

    @Transactional(readOnly = true)
    public List<DestinoFinalDTO> findAll() {
        return destinoFinalRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DestinoFinalDTO findById(Long id) {
        DestinoFinal destino = destinoFinalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Destino final não encontrado"));
        return convertToDTO(destino);
    }

    @Transactional(readOnly = true)
    public List<DestinoFinalDTO> findByTipoDestino(String tipoDestino) {
        return destinoFinalRepository.findByTipoDestino(tipoDestino).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DestinoFinalDTO> findByColetaId(Long idColeta) {
        return destinoFinalRepository.findByColetaIdColeta(idColeta).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Map<String, Long> getQuantidadePorTipoDestino() {
        List<Object[]> results = destinoFinalRepository.findQuantidadePorTipoDestino();
        return results.stream()
                .collect(Collectors.toMap(
                        r -> (String) r[0],
                        r -> ((Number) r[1]).longValue()
                ));
    }

    @Transactional
    public DestinoFinalDTO create(DestinoFinalDTO destinoDTO) {
        Coleta coleta = coletaRepository.findById(destinoDTO.getIdColeta())
                .orElseThrow(() -> new ResourceNotFoundException("Coleta não encontrada"));

        DestinoFinal destino = new DestinoFinal();
        destino.setTipoDestino(destinoDTO.getTipoDestino());
        destino.setLocalDestino(destinoDTO.getLocalDestino());
        destino.setDataRecebimento(destinoDTO.getDataRecebimento());
        destino.setColeta(coleta);

        destino = destinoFinalRepository.save(destino);
        return convertToDTO(destino);
    }

    @Transactional
    public DestinoFinalDTO update(Long id, DestinoFinalDTO destinoDTO) {
        DestinoFinal destino = destinoFinalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Destino final não encontrado"));

        destino.setTipoDestino(destinoDTO.getTipoDestino());
        destino.setLocalDestino(destinoDTO.getLocalDestino());
        destino.setDataRecebimento(destinoDTO.getDataRecebimento());

        if (destinoDTO.getIdColeta() != null) {
            Coleta coleta = coletaRepository.findById(destinoDTO.getIdColeta())
                    .orElseThrow(() -> new ResourceNotFoundException("Coleta não encontrada"));
            destino.setColeta(coleta);
        }

        destino = destinoFinalRepository.save(destino);
        return convertToDTO(destino);
    }

    @Transactional
    public void delete(Long id) {
        if (!destinoFinalRepository.existsById(id)) {
            throw new ResourceNotFoundException("Destino final não encontrado");
        }
        destinoFinalRepository.deleteById(id);
    }

    private DestinoFinalDTO convertToDTO(DestinoFinal destino) {
        DestinoFinalDTO dto = new DestinoFinalDTO();
        dto.setIdDestino(destino.getIdDestino());
        dto.setTipoDestino(destino.getTipoDestino());
        dto.setLocalDestino(destino.getLocalDestino());
        dto.setDataRecebimento(destino.getDataRecebimento());
        dto.setIdColeta(destino.getColeta().getIdColeta());
        return dto;
    }
}