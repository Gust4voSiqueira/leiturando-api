package br.com.leiturando.controller.request.user;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class RegisterUserRequest {
    private String characterName;
    @NotNull
    private String name;
    @NotNull
    private String email;
    @NotNull
    private LocalDateTime dateOfBirth;
    @NotNull
    private String password;
    @NotNull
    private String confirmPassword;
}
