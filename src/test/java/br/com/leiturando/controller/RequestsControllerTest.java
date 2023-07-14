package br.com.leiturando.controller;

import br.com.leiturando.BaseAuthTest;
import br.com.leiturando.controller.response.SendRequestResponse;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import br.com.leiturando.service.RequestsService;
import com.amazonaws.services.kms.model.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static br.com.leiturando.Consts.PASSWORD_DEFAULT;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
public class RequestsControllerTest extends BaseAuthTest {
    @InjectMocks
    RequestsController requestsController;

    @Mock
    RequestsService requestsService;

    @Mock
    SendRequestResponse sendRequestResponse;

    User user;
    User user2;

    @Before
    public void init() {
        user = UserTest.builderUser();
        user2 = User
                .builder()
                .id(2L)
                .name("rogerio")
                .email("rogerio@gmail.com")
                .password(PASSWORD_DEFAULT)
                .imageUrl("Batman")
                .friendships(List.of())
                .build();
        sendRequestResponse = SendRequestResponse
                .builder()
                .requesterId(user.getId())
                .requestedId(user2.getId())
                .build();
    }

    @Test
    public void sendRequestCorrectly() throws Exception {
        when(requestsService.sendRequest(user.getEmail(), user2.getId())).thenReturn(sendRequestResponse);

        var result = requestsController.sendRequest(2L);

        assertEquals(sendRequestResponse, result);
    }

    @Test(expected = RuntimeException.class)
    public void failedToSendRequestForMyUser() throws Exception {
        when(requestsService.sendRequest(user.getEmail(), user.getId()))
                .thenThrow(new RuntimeException("Você não pode enviar uma solicitação para si mesmo."));

        requestsController.sendRequest(user.getId());
    }

    @Test(expected = NotFoundException.class)
    public void failedForNotFoundUser() throws Exception {
        when(requestsService.sendRequest(user.getEmail(), 3L))
                .thenThrow(new NotFoundException("Usuário não encontrado."));

        requestsController.sendRequest(3L);
    }
}
