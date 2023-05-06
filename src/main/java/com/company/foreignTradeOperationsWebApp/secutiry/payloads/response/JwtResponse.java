package com.company.foreignTradeOperationsWebApp.secutiry.payloads.response;

import com.company.foreignTradeOperationsWebApp.models.enums.RoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private RoleEnum role;

    public JwtResponse(String accessToken, Long id, String username, RoleEnum role) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.role = role;
    }
}
