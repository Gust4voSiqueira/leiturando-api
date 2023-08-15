package br.com.leiturando.service.requests;

import br.com.leiturando.controller.response.UserResponse;
import br.com.leiturando.entity.FriendRequests;
import br.com.leiturando.entity.Friendship;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import br.com.leiturando.mapper.RequestMapper;
import br.com.leiturando.mapper.UserMapper;
import br.com.leiturando.repository.FriendRequestRepository;
import br.com.leiturando.repository.UserRepository;
import com.amazonaws.services.kms.model.NotFoundException;
import org.hibernate.procedure.ParameterStrategyException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static br.com.leiturando.Consts.PASSWORD_DEFAULT;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SendRequestsServiceTest {
    @InjectMocks
    SendRequestsService sendRequestsService;
    @Mock
    UserRepository userRepository;
    @Mock
    UserMapper userMapper;
    @Mock
    RequestMapper sendRequestMapper;
    @Mock
    FriendRequestRepository friendRequestRepository;

    User user;

    User user2;
    User user3;
    String email;
    FriendRequests friendRequests;
    UserResponse userResponse;

    List<Friendship> friendships;

    @BeforeEach
    public void init() {
        user = UserTest.builderUser();
        user2 = User.builder()
                .id(2L)
                .name("rogerio")
                .email("rogerio@gmail.com")
                .password(PASSWORD_DEFAULT)
                .image("Batman")
                .friendships(List.of())
                .build();
        user3 = User.builder()
                .id(3L)
                .name("carlos")
                .email("carlos@gmail.com")
                .password(PASSWORD_DEFAULT)
                .image("Flash")
                .friendships(List.of())
                .build();
        friendRequests = FriendRequests.builder()
                .id(1L)
                .requested(user)
                .requester(user2)
                .build();
        friendships = List.of(
                new Friendship(1L, user, user2),
                new Friendship(2L, user, user3)
        );
        userResponse = UserResponse
                .builder()
                .id(user2.getId())
                .name(user2.getName())
                .image(user2.getImage())
                .build();
        email = user.getEmail();
    }

    @Test
    void sendRequestCorrectly() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(userRepository.findById(user2.getId())).thenReturn(Optional.ofNullable(user2));
        when(sendRequestMapper.createRequest(user, user2)).thenReturn(friendRequests);
        when(userMapper.userToResponse(user2)).thenReturn(userResponse);

        var result = sendRequestsService.sendRequest(email, user2.getId());

        Assertions.assertEquals(userResponse, result);
    }

    @Test
    void failedToSendRequestToYourUser() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));

        Long id = user.getId();

        ParameterStrategyException exception = Assertions.assertThrows(ParameterStrategyException.class,
                () -> sendRequestsService.sendRequest(email, id));

        Assertions.assertEquals("Você não pode enviar uma solicitação para si mesmo.", exception.getLocalizedMessage());
    }

    @Test
    void failedToSendRequestToUserNotExists() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(userRepository.findById(5L)).thenReturn(Optional.empty());


        NotFoundException exception = Assertions.assertThrows(NotFoundException.class,
                () -> sendRequestsService.sendRequest(email, 5L));

        Assertions.assertEquals("Usuário não encontrado.", exception.getErrorMessage());
    }
}
