package br.com.leiturando.controller.response.math;

import br.com.leiturando.domain.OperationsMath;
import lombok.*;

@AllArgsConstructor
@Builder
@Getter
public class ListMathResponse {
    private Integer number1;
    private Integer number2;
    private OperationsMath operation;
}
