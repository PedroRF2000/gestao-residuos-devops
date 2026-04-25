package com.esg.gestao.service;

import com.esg.gestao.exception.ResourceNotFoundException;
import com.esg.gestao.model.dto.LoginDTO;
import com.esg.gestao.model.dto.TokenDTO;
import com.esg.gestao.model.dto.UsuarioDTO;
import com.esg.gestao.model.entity.Usuario;
import com.esg.gestao.repository.UsuarioRepository;
import com.esg.gestao.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public TokenDTO login(LoginDTO loginDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
        );

        Usuario usuario = usuarioRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        String token = jwtUtil.generateToken(usuario);
        return new TokenDTO(token, usuario.getUsername(), usuario.getRole().name());
    }

    @Transactional
    public UsuarioDTO register(UsuarioDTO usuarioDTO) {
        if (usuarioRepository.existsByUsername(usuarioDTO.getUsername())) {
            throw new IllegalArgumentException("Username já existe");
        }
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new IllegalArgumentException("Email já existe");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setNomeCompleto(usuarioDTO.getNomeCompleto());
        usuario.setRole(usuarioDTO.getRole());
        usuario.setAtivo(true);

        usuario = usuarioRepository.save(usuario);

        UsuarioDTO result = new UsuarioDTO();
        result.setIdUsuario(usuario.getIdUsuario());
        result.setUsername(usuario.getUsername());
        result.setEmail(usuario.getEmail());
        result.setNomeCompleto(usuario.getNomeCompleto());
        result.setRole(usuario.getRole());

        return result;
    }
}
