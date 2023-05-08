package com.company.foreignTradeOperationsWebApp.payloads.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class LogInRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
