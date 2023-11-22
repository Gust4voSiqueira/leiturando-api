package br.com.leiturando.mapper;

import br.com.leiturando.controller.response.GameResponse;
import br.com.leiturando.controller.response.GameResponseTest;
import br.com.leiturando.controller.response.connectwords.FinallyConnectWordsResponse;
import br.com.leiturando.controller.response.connectwords.FinallyConnectWordsResponseTest;
import br.com.leiturando.controller.response.connectwords.ListConnectWordsResponse;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import br.com.leiturando.entity.Words;
import br.com.leiturando.entity.WordsTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class ConnectWordsMapperTest {
    @InjectMocks
    ConnectWordsMapper connectWordsMapper;
    Words words;
    FinallyConnectWordsResponse finallyConnectWordsResponse;
    GameResponse gameResponse;
    User user;

    @BeforeEach
    public void init() {
        words = WordsTest.builderWords();
        gameResponse = GameResponseTest.builderGameResponse();
        user = UserTest.builderUser();
        finallyConnectWordsResponse = FinallyConnectWordsResponseTest.builderFinallyConnectWordsResponse();
    }

    @Test
    void setWordToResponse() {
        ListConnectWordsResponse result = connectWordsMapper.wordToResponse(words);
        ListConnectWordsResponse expected = ListConnectWordsResponse
                .builder()
                .id(words.getId())
                .word(words.getWord())
                .build();

        Assertions.assertEquals(expected.getId(), result.getId());
        Assertions.assertEquals(expected.getWord(), result.getWord());
    }
    @Test
    void setFinallyConnectWordsResponse() {
        var result = connectWordsMapper.finallyConnectWordsResponse(words.getWord(), true);

        Assertions.assertEquals(gameResponse.getContent(), result.getContent());
        Assertions.assertEquals(gameResponse.isCorrect(), result.isCorrect());
    }

    @Test
    void setFinallyConnectWords() {
        var result = connectWordsMapper.finallyConnectWords(List.of(gameResponse), user, 5);

        Assertions.assertEquals(finallyConnectWordsResponse.getLevel(), result.getLevel());
        Assertions.assertEquals(finallyConnectWordsResponse.getWrong(), result.getWrong());
        Assertions.assertEquals(finallyConnectWordsResponse.getMatches(), result.getMatches());
        Assertions.assertEquals(finallyConnectWordsResponse.getCorrect(), result.getCorrect());
        Assertions.assertEquals(finallyConnectWordsResponse.getBreakthrough(), result.getBreakthrough());
        Assertions.assertEquals(finallyConnectWordsResponse.getScore(), result.getScore());
    }

}
