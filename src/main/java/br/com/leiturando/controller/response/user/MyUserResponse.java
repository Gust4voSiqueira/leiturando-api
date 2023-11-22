package br.com.leiturando.controller.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class MyUserResponse {
    private String image;
    private String email;
    private String name;
    private Integer level;
    private Integer breakthrough;
    private LocalDateTime createdAt;
    private Integer matches;
    private Integer correct;
    private Integer wrong;
}
