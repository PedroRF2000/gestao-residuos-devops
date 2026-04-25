package com.esg.gestao.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro que intercepta todas as requisições HTTP e valida o token JWT
 * Se o token for válido, autentica o usuário no contexto do Spring Security
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Extrai o header Authorization
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // Se não houver header ou não começar com "Bearer ", continua sem autenticar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extrai o token (remove "Bearer " do início)
        jwt = authHeader.substring(7);

        try {
            // Extrai o username do token
            username = jwtUtil.extractUsername(jwt);

            // Se o username existe e não há autenticação no contexto
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Carrega os detalhes do usuário do banco
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                // Valida o token
                if (jwtUtil.validateToken(jwt, userDetails)) {
                    // Cria o objeto de autenticação
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    // Adiciona detalhes da requisição
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Define a autenticação no contexto do Spring Security
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Log do erro (em produção, use um logger apropriado)
            System.err.println("Erro ao processar JWT: " + e.getMessage());
        }

        // Continua a cadeia de filtros
        filterChain.doFilter(request, response);
    }
}