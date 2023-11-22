package br.com.leiturando.controller.response.connectwords;

import lombok.*;

@AllArgsConstructor
@Builder
@Getter
public class ListConnectWordsResponse {
    private Long id;
    private String word;
}
