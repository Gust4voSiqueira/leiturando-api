package br.com.leiturando.controller.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyUserDataResponse {
    private String name;
    private LocalDateTime createdAt;
    private Integer level;
    private Integer breakthrough;
    private Integer matches;
    private Integer correct;
    private Integer wrong;
}
