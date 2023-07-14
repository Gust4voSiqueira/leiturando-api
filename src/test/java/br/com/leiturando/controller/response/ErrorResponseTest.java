package br.com.leiturando.controller.response;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ErrorResponseTest {
    public static ErrorResponse builderErrorResponse() {
        return ErrorResponse
                .builder()
                .message("Mensagem de erro")
                .build();
    }

    @Test
    public void buildRegisterErrorResponseCorrectly() {
        var errorResponse = builderErrorResponse();
        String expected = "Mensagem de erro";

        Assert.assertEquals(expected, errorResponse.getMessage());
    }
}
