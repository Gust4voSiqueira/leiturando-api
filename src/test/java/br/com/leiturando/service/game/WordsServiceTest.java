package br.com.leiturando.service.game;

import br.com.leiturando.controller.request.words.FinallyWordsRequest;
import br.com.leiturando.controller.request.words.FinallyWordsRequestTest;
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
import br.com.leiturando.mapper.WordsMapper;
import br.com.leiturando.repository.UserRepository;
import br.com.leiturando.repository.WordsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class WordsServiceTest {

    @InjectMocks
    WordsService wordsService;

    @Mock
    UserRepository userRepository;

    @Mock
    WordsRepository wordsRepository;

    @Mock
    WordsMapper wordsMapper;
    User user;
    Words word;
    FinallyWordsRequest finallyWordsRequest;
    FinallyWordsResponse finallyWordsResponse;
    ListWordsResponse listWordsResponse;
    GameResponse gameResponse;

    @BeforeEach
    public void init() {
        user = UserTest.builderUser();
        word = WordsTest.builderWords();
        listWordsResponse = ListWordsResponseTest.builderListWordsResponse();
        gameResponse = GameResponseTest.builderGameResponse();
        finallyWordsRequest = FinallyWordsRequestTest.builderFinallyWordsRequest();
        finallyWordsResponse = FinallyWordsResponseTest.builderFinallyWordsResponse();
    }

    @Test
    void getWordsCorrectly() {
        when(wordsRepository.randWords()).thenReturn(List.of(word));
        when(wordsMapper.wordsToResponse(word)).thenReturn(listWordsResponse);

        var result = wordsService.getWords();
        var expected = List.of(listWordsResponse);

        Assertions.assertEquals(expected, result);
    }

    @Test
    void finallyWordsCorrectly() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(wordsRepository.findById(word.getId())).thenReturn(Optional.of(word));
        when(userRepository.save(user)).thenReturn(user);
        when(wordsMapper.createWordsResponse(word.getWord(), true)).thenReturn(gameResponse);
        when(wordsMapper.finallyWords(List.of(gameResponse), user, 2)).thenReturn(finallyWordsResponse);

        var result = wordsService.finallyWordsService(user.getEmail(), finallyWordsRequest);

        Assertions.assertEquals(finallyWordsResponse, result);
    }
}
