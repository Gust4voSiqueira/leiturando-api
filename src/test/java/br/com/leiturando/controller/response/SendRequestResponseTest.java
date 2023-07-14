package br.com.leiturando.controller.response;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class SendRequestResponseTest {
    public static SendRequestResponse builderSendRequestResponse() {
        return SendRequestResponse
                .builder()
                .requesterId(1L)
                .requestedId(2L)
                .build();
    }

    @Test
    public void buildSendRequestResponseCorrectly() {
        var sendRequestResponse = builderSendRequestResponse();

        Assert.assertNotNull(sendRequestResponse.getRequestedId());
        Assert.assertNotNull(sendRequestResponse.getRequesterId());
    }
}
