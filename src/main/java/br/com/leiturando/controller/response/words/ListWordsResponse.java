package br.com.leiturando.controller.response.words;


import lombok.*;

@AllArgsConstructor
@Builder
@Getter
public class ListWordsResponse {
    private Long id;
    private String word;
}
