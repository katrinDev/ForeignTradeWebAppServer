package com.company.foreignTradeOperationsWebApp.payloads.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SignUpRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(min = 5, max = 40)
    private String password;

    @NotBlank
    @Size(min = 6, max = 35)
    @Email
    private String workEmail;
}
