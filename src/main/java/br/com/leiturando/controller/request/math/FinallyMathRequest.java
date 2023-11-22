package br.com.leiturando.controller.request.math;

import br.com.leiturando.domain.OperationsMath;
import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
@Builder
public class FinallyMathRequest {
    @NotNull
    private Integer number1;
    @NotNull
    private Integer number2;
    @NotNull
    private Integer response;
    @NotNull
    private OperationsMath operation;
}
