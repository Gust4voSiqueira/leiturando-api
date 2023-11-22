package br.com.leiturando.controller.request.connectwords;

import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
@Builder
public class FinallyConnectWordsRequest {

    @NotNull
    private Long idWord;
    @NotNull
    private String response;
}
