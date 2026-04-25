package com.esg.gestao.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {
    private String token;
    private String type = "Bearer";
    private String username;
    private String role;

    public TokenDTO(String token, String username, String role) {
        this.token = token;
        this.username = username;
        this.role = role;
    }
}