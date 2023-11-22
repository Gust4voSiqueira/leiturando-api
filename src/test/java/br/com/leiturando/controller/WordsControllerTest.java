package br.com.leiturando.controller;

import br.com.leiturando.BaseAuthTest;
import br.com.leiturando.controller.request.words.FinallyWordsRequest;
import br.com.leiturando.controller.request.words.FinallyWordsRequestTest;
import br.com.leiturando.controller.response.words.FinallyWordsResponse;
import br.com.leiturando.controller.response.words.FinallyWordsResponseTest;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import br.com.leiturando.service.game.WordsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WordsControllerTest extends BaseAuthTest {
    @InjectMocks
    WordsController wordsController;
    @Mock
    WordsService wordsService;

    User user;
    FinallyWordsRequest finallyWordsRequest;
    FinallyWordsResponse finallyWordsResponse;

    @BeforeEach
    void init() {
        user = UserTest.builderUser();
        finallyWordsRequest = FinallyWordsRequestTest.builderFinallyWordsRequest();
        finallyWordsResponse = FinallyWordsResponseTest.builderFinallyWordsResponse();
    }

    @Test
    void getWordsSuccess() {
        when(wordsService.getWords()).thenReturn(List.of());

        var result = wordsController.getWords();

        Assertions.assertNotNull(result);
    }

    @Test
    void failedToCorrectWordsWithoutToken() {
        when(wordsService.finallyWordsService(user.getEmail(), finallyWordsRequest)).thenReturn(finallyWordsResponse);

        var result = wordsController.correctWords(finallyWordsRequest);

        Assertions.assertEquals(finallyWordsResponse, result);
    }
}
