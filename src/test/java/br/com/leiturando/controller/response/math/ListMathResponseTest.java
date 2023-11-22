package br.com.leiturando.controller.response.math;

import br.com.leiturando.domain.OperationsMath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ListMathResponseTest {
    public static ListMathResponse builderListMathResponse() {
        return ListMathResponse
                .builder()
                .number1(1)
                .number2(1)
                .operation(OperationsMath.ADDITION)
                .build();
    }

    @Test
    void buildListMathResponseCorrectly() {
        var listMathResponse = builderListMathResponse();

        Assertions.assertNotNull(listMathResponse.getNumber1());
        Assertions.assertNotNull(listMathResponse.getNumber2());
        Assertions.assertNotNull(listMathResponse.getOperation());
    }
}
