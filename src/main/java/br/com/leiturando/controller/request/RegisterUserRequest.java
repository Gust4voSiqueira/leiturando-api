package br.com.leiturando.controller.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequest {
    private String characterName;
    private String name;
    private String email;
    private LocalDateTime dateOfBirth;
    private String password;
    private String confirmPassword;
}
