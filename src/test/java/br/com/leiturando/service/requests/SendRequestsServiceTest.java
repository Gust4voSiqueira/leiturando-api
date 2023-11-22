package br.com.leiturando.service.requests;

import br.com.leiturando.controller.response.user.UserResponse;
import br.com.leiturando.entity.FriendRequests;
import br.com.leiturando.entity.Friendship;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import br.com.leiturando.mapper.RequestMapper;
import br.com.leiturando.mapper.UserMapper;
import br.com.leiturando.repository.FriendRequestRepository;
import br.com.leiturando.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

        var result = sendRequestsService.sendRequest(email, user2.getId());
        var expected = new ResponseEntity<>(HttpStatus.CREATED);

        Assertions.assertEquals(expected, result);
    }

    @Test
    void failedToSendRequestToYourUser() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));

        Long id = user.getId();

        var result = sendRequestsService.sendRequest(email, id);
        var expected = new ResponseEntity<>("Você não pode enviar uma solicitação para si mesmo.", HttpStatus.BAD_REQUEST);

        Assertions.assertEquals(result, expected);
    }

    @Test
    void failedToSendRequestToUserNotExists() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(userRepository.findById(5L)).thenReturn(Optional.empty());

        var result = sendRequestsService.sendRequest(email, 5L);
        var expected = new ResponseEntity<>("Usuário não encontrado.", HttpStatus.BAD_REQUEST);

        Assertions.assertEquals(expected, result);
    }

    @Test
    void failedToSendRequestAlreadyExists() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(friendRequestRepository.findByRequestedAndRequester(user.getId(), user2.getId())).thenReturn(Optional.of(friendRequests));

        var result = sendRequestsService.sendRequest(email, 2L);
        var expected = new ResponseEntity<>("Solicitação já enviada.", HttpStatus.BAD_REQUEST);

        Assertions.assertEquals(expected, result);
    }
}
