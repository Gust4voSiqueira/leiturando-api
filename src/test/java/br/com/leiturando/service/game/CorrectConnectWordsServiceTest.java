package br.com.leiturando.service.game;

import br.com.leiturando.controller.request.connectwords.FinallyConnectWordsRequest;
import br.com.leiturando.controller.request.connectwords.FinallyConnectWordsRequestTest;
import br.com.leiturando.controller.response.GameResponse;
import br.com.leiturando.controller.response.GameResponseTest;
import br.com.leiturando.controller.response.connectwords.FinallyConnectWordsResponse;
import br.com.leiturando.controller.response.connectwords.FinallyConnectWordsResponseTest;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import br.com.leiturando.entity.Words;
import br.com.leiturando.entity.WordsTest;
import br.com.leiturando.mapper.ConnectWordsMapper;
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
class CorrectConnectWordsServiceTest {
    @InjectMocks
    CorrectConnectWordsService connectWordsService;

    @Mock
    WordsRepository wordsRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ConnectWordsMapper connectWordsMapper;

    User user;
    FinallyConnectWordsRequest finallyConnectWordsRequest;
    Words words;
    GameResponse gameResponse;
    FinallyConnectWordsResponse finallyConnectWordsResponse;

    @BeforeEach
    public void init() {
        user = UserTest.builderUser();
        finallyConnectWordsRequest = FinallyConnectWordsRequestTest.builderFinallyConnectWordsResponse();
        words = WordsTest.builderWords();
        gameResponse = GameResponseTest.builderGameResponse();
        finallyConnectWordsResponse = FinallyConnectWordsResponseTest.builderFinallyConnectWordsResponse();
    }

    @Test
    void correctConnectWordsCorrectly() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(wordsRepository.findById(words.getId())).thenReturn(Optional.of(words));
        when(userRepository.save(user)).thenReturn(user);
        when(connectWordsMapper.finallyConnectWords(List.of(gameResponse), user, 1)).thenReturn(finallyConnectWordsResponse);
        when(connectWordsMapper.finallyConnectWordsResponse(words.getWord(), true)).thenReturn(gameResponse);

        var result = connectWordsService.finallyConnectWords(user.getEmail(), List.of(finallyConnectWordsRequest));

        Assertions.assertEquals(finallyConnectWordsResponse, result);
    }
}
