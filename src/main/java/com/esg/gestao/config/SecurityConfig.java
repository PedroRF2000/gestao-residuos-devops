package com.esg.gestao.config;

import com.esg.gestao.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/actuator/health").permitAll()

                        // Endpoints de Locais
                        .requestMatchers(HttpMethod.GET, "/locais/**").hasAnyRole("ADMIN", "OPERADOR", "USUARIO")
                        .requestMatchers(HttpMethod.POST, "/locais/**").hasAnyRole("ADMIN", "OPERADOR")
                        .requestMatchers(HttpMethod.PUT, "/locais/**").hasAnyRole("ADMIN", "OPERADOR")
                        .requestMatchers(HttpMethod.DELETE, "/locais/**").hasRole("ADMIN")

                        // Endpoints de Transportadores
                        .requestMatchers(HttpMethod.GET, "/transportadores/**").hasAnyRole("ADMIN", "OPERADOR", "USUARIO")
                        .requestMatchers(HttpMethod.POST, "/transportadores/**").hasAnyRole("ADMIN", "OPERADOR")
                        .requestMatchers(HttpMethod.PUT, "/transportadores/**").hasAnyRole("ADMIN", "OPERADOR")
                        .requestMatchers(HttpMethod.DELETE, "/transportadores/**").hasRole("ADMIN")

                        // Endpoints de Resíduos
                        .requestMatchers(HttpMethod.GET, "/residuos/**").hasAnyRole("ADMIN", "OPERADOR", "USUARIO")
                        .requestMatchers(HttpMethod.POST, "/residuos/**").hasAnyRole("ADMIN", "OPERADOR")
                        .requestMatchers(HttpMethod.PUT, "/residuos/**").hasAnyRole("ADMIN", "OPERADOR")
                        .requestMatchers(HttpMethod.DELETE, "/residuos/**").hasRole("ADMIN")

                        // Endpoints de Coletas
                        .requestMatchers(HttpMethod.GET, "/coletas/**").hasAnyRole("ADMIN", "OPERADOR", "USUARIO")
                        .requestMatchers(HttpMethod.POST, "/coletas/**").hasAnyRole("ADMIN", "OPERADOR")
                        .requestMatchers(HttpMethod.PUT, "/coletas/**").hasAnyRole("ADMIN", "OPERADOR")
                        .requestMatchers(HttpMethod.DELETE, "/coletas/**").hasRole("ADMIN")

                        // Endpoints de Destinos Finais
                        .requestMatchers(HttpMethod.GET, "/destinos/**").hasAnyRole("ADMIN", "OPERADOR", "USUARIO")
                        .requestMatchers(HttpMethod.POST, "/destinos/**").hasAnyRole("ADMIN", "OPERADOR")
                        .requestMatchers(HttpMethod.PUT, "/destinos/**").hasAnyRole("ADMIN", "OPERADOR")
                        .requestMatchers(HttpMethod.DELETE, "/destinos/**").hasRole("ADMIN")

                        // Qualquer outra requisição requer autenticação
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}