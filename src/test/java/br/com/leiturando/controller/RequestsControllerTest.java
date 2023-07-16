package br.com.leiturando.controller;

import br.com.leiturando.BaseAuthTest;
import br.com.leiturando.controller.response.SendRequestResponse;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import br.com.leiturando.service.RequestsService;
import com.amazonaws.services.kms.model.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static br.com.leiturando.Consts.PASSWORD_DEFAULT;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestsControllerTest extends BaseAuthTest {
    @InjectMocks
    RequestsController requestsController;

    @Mock
    RequestsService requestsService;

    @Mock
    SendRequestResponse sendRequestResponse;

    User user;
    User user2;

    @BeforeEach
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
    void sendRequestCorrectly() throws Exception {
        when(requestsService.sendRequest(user.getEmail(), user2.getId())).thenReturn(sendRequestResponse);

        var result = requestsController.sendRequest(2L);

        Assertions.assertEquals(sendRequestResponse, result);
    }

    @Test
    void failedToSendRequestForMyUser() {
        when(requestsService.sendRequest(user.getEmail(), user.getId()))
                .thenThrow(new RuntimeException("Você não pode enviar uma solicitação para si mesmo."));

        var id = user.getId();

        Exception exception = Assertions.assertThrows(RuntimeException.class,
                () -> requestsController.sendRequest(id));

        Assertions.assertNotNull(exception.getMessage());
    }

    @Test
    void failedForNotFoundUser() {
        when(requestsService.sendRequest(user.getEmail(), 3L))
                .thenThrow(new NotFoundException("Usuário não encontrado."));

        Exception exception = Assertions.assertThrows(NotFoundException.class,
                () -> requestsController.sendRequest(3L));

        Assertions.assertNotNull(exception.getMessage());
    }
}
