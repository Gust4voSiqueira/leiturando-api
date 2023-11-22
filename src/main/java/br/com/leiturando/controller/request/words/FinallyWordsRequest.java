package br.com.leiturando.controller.request.words;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class FinallyWordsRequest {

    private List<Long> wordIds;
    private List<String> responses;
}
