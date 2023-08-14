package br.com.leiturando.controller.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
    private String name;
    private String email;
    private LocalDateTime dateOfBirth;
    private String characterName;
}
