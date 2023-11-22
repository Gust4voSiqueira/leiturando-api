package br.com.leiturando.controller.response.words;

import br.com.leiturando.controller.response.GameResponse;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class FinallyWordsResponse {
    private List<GameResponse> words;
    private Integer level;
    private Integer breakthrough;
    private Integer matches;
    private Integer correct;
    private Integer wrong;
    private Integer score;
}
