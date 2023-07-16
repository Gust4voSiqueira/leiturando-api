package br.com.leiturando.controller.response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ErrorResponseTest {
    public static ErrorResponse builderErrorResponse() {
        return ErrorResponse
                .builder()
                .message("Mensagem de erro")
                .build();
    }

    @Test
    void buildRegisterErrorResponseCorrectly() {
        var errorResponse = builderErrorResponse();
        String expected = "Mensagem de erro";

        Assertions.assertEquals(expected, errorResponse.getMessage());
    }
}
