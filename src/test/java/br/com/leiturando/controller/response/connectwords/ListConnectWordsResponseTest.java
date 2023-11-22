package br.com.leiturando.controller.response.connectwords;

import br.com.leiturando.Consts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ListConnectWordsResponseTest {
    public static ListConnectWordsResponse builderListConnectWordsResponse() {
        return ListConnectWordsResponse
                .builder()
                .id(1L)
                .word(Consts.WORD_DEFAULT)
                .build();
    }

    @Test
    void buildConnectWordsResponseCorrectly() {
        var listConnectWordsResponse = builderListConnectWordsResponse();

        Assertions.assertNotNull(listConnectWordsResponse.getId());
        Assertions.assertNotNull(listConnectWordsResponse.getWord());
    }
}
