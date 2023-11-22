package br.com.leiturando.controller.response.words;

import br.com.leiturando.Consts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ListWordsResponseTest {
    public static ListWordsResponse builderListWordsResponse() {
        return ListWordsResponse
                .builder()
                .id(1L)
                .word(Consts.WORD_DEFAULT)
                .build();
    }

    @Test
    void buildListWordsResponseCorrectly() {
        var listWordsResponse = builderListWordsResponse();

        Assertions.assertNotNull(listWordsResponse.getId());
        Assertions.assertNotNull(listWordsResponse.getWord());
    }
}
