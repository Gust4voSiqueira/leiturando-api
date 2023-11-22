package br.com.leiturando.controller.response.connectwords;

import br.com.leiturando.controller.response.GameResponseTest;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class FinallyConnectWordsResponseTest {
    public static FinallyConnectWordsResponse builderFinallyConnectWordsResponse() {
        User user = UserTest.builderUser();

        return FinallyConnectWordsResponse
                .builder()
                .words(List.of(GameResponseTest.builderGameResponse()))
                .level(user.getLevel())
                .breakthrough(user.getBreakthrough())
                .matches(user.getMatches())
                .correct(user.getCorrect())
                .wrong(user.getWrong())
                .score(5)
                .build();
    }

    @Test
    void buildFinallyConnectWordsResponseCorrectly() {
        var finallyConnectWordsResponse = builderFinallyConnectWordsResponse();

        Assertions.assertNotNull(finallyConnectWordsResponse.getWords());
        Assertions.assertNotNull(finallyConnectWordsResponse.getBreakthrough());
        Assertions.assertNotNull(finallyConnectWordsResponse.getLevel());
        Assertions.assertNotNull(finallyConnectWordsResponse.getBreakthrough());
        Assertions.assertNotNull(finallyConnectWordsResponse.getMatches());
        Assertions.assertNotNull(finallyConnectWordsResponse.getCorrect());
        Assertions.assertNotNull(finallyConnectWordsResponse.getWrong());
        Assertions.assertNotNull(finallyConnectWordsResponse.getScore());
    }
}
