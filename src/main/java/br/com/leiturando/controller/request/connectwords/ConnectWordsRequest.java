package br.com.leiturando.controller.request.connectwords;

import br.com.leiturando.entity.Words;
import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
@Builder
public class ConnectWordsRequest {
    @NotNull
    private Words word;

    @NotNull
    private String response;
}
