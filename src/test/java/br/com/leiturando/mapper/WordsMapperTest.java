package br.com.leiturando.mapper;

import br.com.leiturando.controller.response.GameResponse;
import br.com.leiturando.controller.response.GameResponseTest;
import br.com.leiturando.controller.response.words.FinallyWordsResponse;
import br.com.leiturando.controller.response.words.FinallyWordsResponseTest;
import br.com.leiturando.controller.response.words.ListWordsResponse;
import br.com.leiturando.controller.response.words.ListWordsResponseTest;
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
class WordsMapperTest {
    @InjectMocks
    WordsMapper wordsMapper;

    User user;
    Words words;
    ListWordsResponse listWordsResponse;
    GameResponse gameResponse;
    FinallyWordsResponse finallyWordsResponse;

    @BeforeEach
    public void init() {
        user = UserTest.builderUser();
        words = WordsTest.builderWords();
        listWordsResponse = ListWordsResponseTest.builderListWordsResponse();
        gameResponse = GameResponseTest.builderGameResponse();
        finallyWordsResponse = FinallyWordsResponseTest.builderFinallyWordsResponse();
    }

    @Test
    void setWordsToResponse() {
        var result = wordsMapper.wordsToResponse(words);

        Assertions.assertEquals(listWordsResponse.getId(), result.getId());
        Assertions.assertEquals(listWordsResponse.getWord(), result.getWord());
    }

    @Test
    void setFinallyWords() {
        var result = wordsMapper.finallyWords(List.of(gameResponse), user, 5);

        Assertions.assertEquals(finallyWordsResponse.getLevel(), result.getLevel());
        Assertions.assertEquals(finallyWordsResponse.getWrong(), result.getWrong());
        Assertions.assertEquals(finallyWordsResponse.getMatches(), result.getMatches());
        Assertions.assertEquals(finallyWordsResponse.getCorrect(), result.getCorrect());
        Assertions.assertEquals(finallyWordsResponse.getBreakthrough(), result.getBreakthrough());
        Assertions.assertEquals(finallyWordsResponse.getScore(), result.getScore());
    }

    @Test
    void setCreateWordsResponse() {
        var result = wordsMapper.createWordsResponse(words.getWord(), true);

        Assertions.assertEquals(gameResponse.getContent(), result.getContent());
        Assertions.assertEquals(gameResponse.isCorrect(), result.isCorrect());
    }
}
