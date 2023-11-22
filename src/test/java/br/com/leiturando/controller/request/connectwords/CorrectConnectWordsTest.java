package br.com.leiturando.controller.request.connectwords;

import br.com.leiturando.Consts;
import br.com.leiturando.entity.Words;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CorrectConnectWordsTest {
    public static ConnectWordsRequest builderConnectWordsResponse() {
        return ConnectWordsRequest
                .builder()
                .response(Consts.WORD_DEFAULT)
                .word(new Words(1L, Consts.WORD_DEFAULT))
                .build();
    }

    @Test
    void buildConnectWordsResponseCorrectly() {
        var connectWordsResponse = builderConnectWordsResponse();

        Assertions.assertNotNull(connectWordsResponse.getResponse());
        Assertions.assertNotNull(connectWordsResponse.getWord());
    }
}
