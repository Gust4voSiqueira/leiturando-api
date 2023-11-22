package br.com.leiturando.controller.response.words;

import br.com.leiturando.Consts;
import br.com.leiturando.controller.response.GameResponse;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class FinallyWordsResponseTest {
    public static FinallyWordsResponse builderFinallyWordsResponse() {
        User user = UserTest.builderUser();

        return FinallyWordsResponse
                .builder()
                .words(List.of(GameResponse.builder()
                                .content(Consts.WORD_DEFAULT)
                                .isCorrect(true)
                        .build()))
                .level(user.getLevel())
                .breakthrough(user.getBreakthrough())
                .matches(user.getMatches())
                .correct(user.getCorrect())
                .wrong(user.getWrong())
                .score(5)
                .build();
    }

    @Test
    void buildFinallyWordsResponseCorrectly() {
        var finallyWordsResponse = builderFinallyWordsResponse();

        Assertions.assertNotNull(finallyWordsResponse.getWords());
        Assertions.assertNotNull(finallyWordsResponse.getBreakthrough());
        Assertions.assertNotNull(finallyWordsResponse.getLevel());
        Assertions.assertNotNull(finallyWordsResponse.getBreakthrough());
        Assertions.assertNotNull(finallyWordsResponse.getMatches());
        Assertions.assertNotNull(finallyWordsResponse.getCorrect());
        Assertions.assertNotNull(finallyWordsResponse.getWrong());
        Assertions.assertNotNull(finallyWordsResponse.getScore());
    }
}
