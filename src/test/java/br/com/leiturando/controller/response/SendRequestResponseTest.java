package br.com.leiturando.controller.response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SendRequestResponseTest {
    public static SendRequestResponse builderSendRequestResponse() {
        return SendRequestResponse
                .builder()
                .requesterId(1L)
                .requestedId(2L)
                .build();
    }

    @Test
    void buildSendRequestResponseCorrectly() {
        var sendRequestResponse = builderSendRequestResponse();

        Assertions.assertNotNull(sendRequestResponse.getRequestedId());
        Assertions.assertNotNull(sendRequestResponse.getRequesterId());
    }
}
