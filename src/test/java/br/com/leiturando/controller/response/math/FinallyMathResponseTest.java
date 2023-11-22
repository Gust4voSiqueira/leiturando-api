package br.com.leiturando.controller.response.math;

import br.com.leiturando.controller.response.GameResponse;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class FinallyMathResponseTest {
    public static FinallyMathResponse builderFinallyMathResponse() {
        User user = UserTest.builderUser();

        return FinallyMathResponse
                .builder()
                .results(List.of(GameResponse.builder()
                        .content("1 + 2 = 3")
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
    void buildFinallyMathResponseResponseCorrectly() {
        var finallyMathResponse = builderFinallyMathResponse();

        Assertions.assertNotNull(finallyMathResponse.getResults());
        Assertions.assertNotNull(finallyMathResponse.getBreakthrough());
        Assertions.assertNotNull(finallyMathResponse.getLevel());
        Assertions.assertNotNull(finallyMathResponse.getBreakthrough());
        Assertions.assertNotNull(finallyMathResponse.getMatches());
        Assertions.assertNotNull(finallyMathResponse.getCorrect());
        Assertions.assertNotNull(finallyMathResponse.getWrong());
        Assertions.assertNotNull(finallyMathResponse.getScore());
    }
}
