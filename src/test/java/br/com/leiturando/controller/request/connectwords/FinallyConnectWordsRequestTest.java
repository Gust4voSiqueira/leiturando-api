package br.com.leiturando.controller.request.connectwords;

import br.com.leiturando.Consts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FinallyConnectWordsRequestTest {
    public static FinallyConnectWordsRequest builderFinallyConnectWordsResponse() {
        return FinallyConnectWordsRequest
                .builder()
                .idWord(1L)
                .response(Consts.WORD_DEFAULT)
                .build();
    }

    @Test
    void buildConnectWordsResponseCorrectly() {
        var finallyConnectWordsResponse = builderFinallyConnectWordsResponse();

        Assertions.assertNotNull(finallyConnectWordsResponse.getIdWord());
        Assertions.assertNotNull(finallyConnectWordsResponse.getResponse());
    }
}
