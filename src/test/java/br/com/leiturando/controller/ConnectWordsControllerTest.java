package br.com.leiturando.controller;

import br.com.leiturando.BaseAuthTest;
import br.com.leiturando.controller.request.connectwords.FinallyConnectWordsRequest;
import br.com.leiturando.controller.request.connectwords.FinallyConnectWordsRequestTest;
import br.com.leiturando.controller.response.connectwords.FinallyConnectWordsResponse;
import br.com.leiturando.controller.response.connectwords.FinallyConnectWordsResponseTest;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import br.com.leiturando.service.game.ConnectWordsService;
import br.com.leiturando.service.game.CorrectConnectWordsService;
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
class ConnectWordsControllerTest extends BaseAuthTest {
    @InjectMocks
    ConnectWordsController connectWordsController;
    @Mock
    ConnectWordsService connectWordsService;
    @Mock
    CorrectConnectWordsService correctConnectWordsService;

    FinallyConnectWordsRequest finallyConnectWordsRequest;
    User user;
    FinallyConnectWordsResponse finallyWordsResponse;

    @BeforeEach
    void init() {
        finallyConnectWordsRequest = FinallyConnectWordsRequestTest.builderFinallyConnectWordsResponse();
        user = UserTest.builderUser();
        finallyConnectWordsRequest = FinallyConnectWordsRequestTest.builderFinallyConnectWordsResponse();
        finallyWordsResponse = FinallyConnectWordsResponseTest.builderFinallyConnectWordsResponse();
    }

    @Test
    void getWordsToConnectSuccess() {
        when(connectWordsService.getWordsToConnect()).thenReturn(List.of());

        var result = connectWordsController.getWordsToConnect();

        Assertions.assertNotNull(result);
    }

    @Test
    void failedToCorrectConnectWordsWithoutToken() {
        when(correctConnectWordsService.finallyConnectWords(user.getEmail(), List.of(finallyConnectWordsRequest))).thenReturn(finallyWordsResponse);

        var result = connectWordsController.correctConnectWords(List.of(finallyConnectWordsRequest));

        Assertions.assertEquals(finallyWordsResponse, result);
    }
}
