package br.com.leiturando.controller.request.user;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UpdateUserRequest {
    private String name;
    private LocalDateTime dateOfBirth;
    private String characterName;
    @NotNull
    private String password;
    private String newPassword;
    private String confirmNewPassword;
}
