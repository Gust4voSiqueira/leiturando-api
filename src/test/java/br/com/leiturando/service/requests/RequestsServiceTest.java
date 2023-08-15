package br.com.leiturando.service.requests;

import br.com.leiturando.controller.response.ListRequestsResponse;
import br.com.leiturando.entity.FriendRequests;
import br.com.leiturando.entity.Friendship;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import br.com.leiturando.mapper.RequestMapper;
import br.com.leiturando.repository.FriendRequestRepository;
import br.com.leiturando.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
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
    RequestMapper sendRequestMapper;

    User user;

    User user2;
    User user3;
    User user4;
    FriendRequests friendRequests;
    ListRequestsResponse listRequestsResponse;

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
        user4 = User.builder()
                .id(3L)
                .name("joao")
                .email("joao@gmail.com")
                .password(PASSWORD_DEFAULT)
                .image("Flash")
                .friendships(List.of())
                .build();
        friendRequests = FriendRequests.builder()
                .id(1L)
                .requested(user)
                .requester(user2)
                .build();
        listRequestsResponse = ListRequestsResponse.builder()
                .name(user2.getName())
                .image(user2.getImage())
                .mutualFriends(0)
                .build();
    }


    @Test
    void searchRequestsCorrectly() {
        when(friendRequestRepository.findAllByRequestedId(user.getId())).thenReturn(List.of(friendRequests));
        when(userRepository.findById(user2.getId())).thenReturn(Optional.ofNullable(user2));
        when(sendRequestMapper.myUserResponse(user2, 0)).thenReturn(listRequestsResponse);

        var result = requestsService.searchRequestsReceived(user);
        var expected = List.of(listRequestsResponse);

        Assertions.assertEquals(expected, result);
    }

    @Test
    void searchRequestsCorrectlyInUserWithoutPrompts() {
        when(friendRequestRepository.findAllByRequestedId(user.getId())).thenReturn(List.of());

        var result = requestsService.searchRequestsReceived(user);
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
