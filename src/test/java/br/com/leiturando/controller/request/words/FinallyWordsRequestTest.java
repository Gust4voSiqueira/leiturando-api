package br.com.leiturando.controller.request.words;

import br.com.leiturando.Consts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class FinallyWordsRequestTest {
    public static FinallyWordsRequest builderFinallyWordsRequest() {
        return FinallyWordsRequest
                .builder()
                .wordIds(List.of(1L, 2L))
                .responses(List.of(Consts.WORD_DEFAULT))
                .build();
    }

    @Test
    void buildFinallyMathRequestCorrectly() {
        var finallyWordsRequest = builderFinallyWordsRequest();

        Assertions.assertNotNull(finallyWordsRequest.getWordIds());
        Assertions.assertNotNull(finallyWordsRequest.getResponses());
    }
}
