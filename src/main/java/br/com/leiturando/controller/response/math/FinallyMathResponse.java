package br.com.leiturando.controller.response.math;

import br.com.leiturando.controller.response.GameResponse;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class FinallyMathResponse {
    private List<GameResponse> results;
    private Integer level;
    private Integer breakthrough;
    private Integer matches;
    private Integer correct;
    private Integer wrong;
    private Integer score;
}
