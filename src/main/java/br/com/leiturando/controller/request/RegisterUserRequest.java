package br.com.leiturando.controller.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequest {
    private String characterName;
    private String name;
    private String email;
    private String password;
    private String confirmPassword;
}
