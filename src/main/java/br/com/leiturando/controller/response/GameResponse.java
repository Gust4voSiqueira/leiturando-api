package br.com.leiturando.controller.response;

import lombok.*;

@AllArgsConstructor
@Getter
@Builder
public class GameResponse {
    private String content;
    private boolean isCorrect;
}
