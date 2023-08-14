package br.com.leiturando.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MyUserResponse {
    private String image;
    private String name;
    private Integer level;
    private Integer breakthrough;
    private LocalDateTime createdAt;
    private Integer matches;
    private Integer correct;
    private Integer wrong;
}
