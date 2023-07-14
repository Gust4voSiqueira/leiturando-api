package br.com.leiturando.controller.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserResponse {
    private String urlImage;
    private String name;
    private String email;
}


