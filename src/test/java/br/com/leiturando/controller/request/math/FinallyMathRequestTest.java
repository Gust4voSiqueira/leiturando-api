package br.com.leiturando.controller.request.math;

import br.com.leiturando.domain.OperationsMath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FinallyMathRequestTest {
    public static FinallyMathRequest builderFinallyMathRequest() {
        return FinallyMathRequest
                .builder()
                .number1(1)
                .number2(2)
                .operation(OperationsMath.ADDITION)
                .response(3)
                .build();
    }

    @Test
    void buildFinallyMathRequestCorrectly() {
        var finallyMathRequest = builderFinallyMathRequest();

        Assertions.assertNotNull(finallyMathRequest.getNumber1());
        Assertions.assertNotNull(finallyMathRequest.getNumber2());
        Assertions.assertNotNull(finallyMathRequest.getOperation());
        Assertions.assertNotNull(finallyMathRequest.getResponse());
    }
}
