package br.com.leiturando.controller.response;

import br.com.leiturando.Consts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GameResponseTest {
    public static GameResponse builderGameResponse() {
        return GameResponse
                .builder()
                .content(Consts.WORD_DEFAULT)
                .isCorrect(true)
                .build();
    }

    @Test
    void buildGameResponseCorrectly() {
        var gameResponse = builderGameResponse();

        Assertions.assertNotNull(gameResponse.getContent());
        Assertions.assertTrue(gameResponse.isCorrect());
    }
}
