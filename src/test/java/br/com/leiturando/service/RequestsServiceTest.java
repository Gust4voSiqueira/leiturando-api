package br.com.leiturando.service;

import br.com.leiturando.controller.response.ListRequestsResponse;
import br.com.leiturando.controller.response.SendRequestResponse;
import br.com.leiturando.entity.FriendRequests;
import br.com.leiturando.entity.Friendship;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import br.com.leiturando.mapper.SendRequestMapper;
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
class RequestsServiceTest {
    @InjectMocks
    RequestsService requestsService;

    @Mock
    FriendRequestRepository friendRequestRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    SendRequestMapper sendRequestMapper;

    User user;

    User user2;
    User user3;
    User user4;
    FriendRequests friendRequests;
    ListRequestsResponse listRequestsResponse;
    SendRequestResponse sendRequestResponse;

    List<Friendship> friendships;
    String email;

    @BeforeEach
    public void init() {
        user = UserTest.builderUser();
        user2 = User.builder()
                .id(2L)
                .name("rogerio")
                .email("rogerio@gmail.com")
                .password(PASSWORD_DEFAULT)
                .imageUrl("Batman")
                .friendships(List.of())
                .build();
        user3 = User.builder()
                .id(3L)
                .name("carlos")
                .email("carlos@gmail.com")
                .password(PASSWORD_DEFAULT)
                .imageUrl("Flash")
                .friendships(List.of())
                .build();
        user4 = User.builder()
                .id(3L)
                .name("joao")
                .email("joao@gmail.com")
                .password(PASSWORD_DEFAULT)
                .imageUrl("Flash")
                .friendships(List.of())
                .build();
        friendRequests = FriendRequests.builder()
                .id(1L)
                .requested(user)
                .requester(user2)
                .build();
        listRequestsResponse = ListRequestsResponse.builder()
                .id(1L)
                .name(user2.getName())
                .urlImage(user2.getImageUrl())
                .mutualFriends(1)
                .build();
        friendships = List.of(
                new Friendship(1L, user, user2),
                new Friendship(2L, user, user3)
        );
        sendRequestResponse = SendRequestResponse
                .builder()
                .requestedId(user.getId())
                .requesterId(user2.getId())
                .build();
        email = user.getEmail();
    }

    @Test
    void sendRequestCorrectly() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(userRepository.findById(user2.getId())).thenReturn(Optional.ofNullable(user2));
        when(sendRequestMapper.createRequest(user, user2)).thenReturn(friendRequests);
        when(sendRequestMapper.requestToResponse(friendRequests)).thenReturn(sendRequestResponse);

        var result = requestsService.sendRequest(email, user2.getId());
        var expected = sendRequestResponse;

        Assertions.assertEquals(expected, result);
    }

    @Test
    void failedToSendRequestToYourUser() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));

        Long id = user.getId();

        ParameterStrategyException exception = Assertions.assertThrows(ParameterStrategyException.class,
                () -> requestsService.sendRequest(email, id));

        Assertions.assertEquals("Você não pode enviar uma solicitação para si mesmo.", exception.getLocalizedMessage());
    }

    @Test
    void failedToSendRequestToUserNotExists() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(userRepository.findById(5L)).thenReturn(Optional.empty());


        NotFoundException exception = Assertions.assertThrows(NotFoundException.class,
                () -> requestsService.sendRequest(email, 5L));

        Assertions.assertEquals("Usuário não encontrado.", exception.getErrorMessage());
    }

    @Test
    void searchRequestsCorrectly() {
        when(friendRequestRepository.findAllByRequestedId(user.getId())).thenReturn(List.of(friendRequests));
        when(userRepository.findById(user2.getId())).thenReturn(Optional.ofNullable(user2));
        when(sendRequestMapper.myUserResponse(user2, 0)).thenReturn(listRequestsResponse);

        var result = requestsService.searchRequests(user);
        var expected = List.of(listRequestsResponse);

        Assertions.assertEquals(expected, result);
    }

    @Test
    void searchRequestsCorrectlyInUserWithoutPrompts() {
        when(friendRequestRepository.findAllByRequestedId(user.getId())).thenReturn(List.of());

        var result = requestsService.searchRequests(user);
        var expected = List.of();

        Assertions.assertEquals(expected, result);
    }

    @Test
    void searchMutualFriendsCorrectly() {
        user.setFriendships(List.of(
                new Friendship(1L, user, user2),
                new Friendship(2L, user, user3),
                new Friendship(3L, user, user4)
        ));
        user2.setFriendships(List.of(new Friendship(1L, user, user2), new Friendship(3L, user, user4)));
        user3.setFriendships(List.of(new Friendship(2L, user, user3), new Friendship(3L, user, user4)));

        var result = requestsService.searchMutualFriends(user2, user3);
        var result2 = requestsService.searchMutualFriends(user3, user2);

        Integer expected = 2;

        Assertions.assertEquals(expected, result);
        Assertions.assertEquals(expected, result2);
    }
}
