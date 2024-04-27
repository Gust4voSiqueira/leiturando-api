package br.com.leiturando.service.requests;

import br.com.leiturando.controller.response.requests.ListRequestsResponse;
import br.com.leiturando.controller.response.requests.RecommendedFriendsResponse;
import br.com.leiturando.controller.response.requests.RequestResponse;
import br.com.leiturando.controller.response.user.UserResponse;
import br.com.leiturando.entity.FriendRequests;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import br.com.leiturando.mapper.RequestMapper;
import br.com.leiturando.mapper.UserMapper;
import br.com.leiturando.repository.FriendRequestRepository;
import br.com.leiturando.repository.UserRepository;
import br.com.leiturando.service.FriendshipService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Page;

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
    FriendshipService friendshipService;

    @Mock
    UserRepository userRepository;

    @Mock
    RequestMapper requestMapper;

    @Mock
    UserMapper userMapper;


    User user;
    User user2;
    User user3;
    User user4;
    User user5;
    FriendRequests friendRequests;
    FriendRequests friendRequests2;
    ListRequestsResponse listRequestsResponse;
    ListRequestsResponse listRequestsResponseUser4;
    UserResponse userResponse;
    RequestResponse requestResponse;
    RequestResponse requestResponseWithRecommendedFriend;
    RecommendedFriendsResponse recommendedFriendsResponse;

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
        user5 = User.builder()
                .id(4L)
                .name("felipe")
                .email("felipe@gmail.com")
                .password(PASSWORD_DEFAULT)
                .image("Harry")
                .friendships(List.of())
                .build();
        friendRequests = FriendRequests.builder()
                .id(1L)
                .requested(user)
                .requester(user2)
                .build();
        friendRequests2 = FriendRequests.builder()
                .id(4L)
                .requested(user4)
                .requester(user)
                .build();
        listRequestsResponse = ListRequestsResponse.builder()
                .name(user2.getName())
                .image(user2.getImage())
                .mutualFriends(0)
                .build();
        listRequestsResponseUser4 = ListRequestsResponse.builder()
                .name(user4.getName())
                .image(user4.getImage())
                .mutualFriends(0)
                .build();
        userResponse = UserResponse
                .builder()
                .id(1L)
                .name(user3.getName())
                .image(user3.getImage())
                .build();
        requestResponse = RequestResponse
                .builder()
                .friends(List.of(userResponse))
                .requests(List.of(listRequestsResponse))
                .requestsSend(List.of(listRequestsResponseUser4))
                .usersRecommended(List.of())
                .build();
        recommendedFriendsResponse = RecommendedFriendsResponse
                .builder()
                .id(user2.getId())
                .name(user2.getName())
                .image(user2.getImage())
                .mutualFriends(0)
                .build();
        requestResponseWithRecommendedFriend =  RequestResponse
                .builder()
                .friends(List.of())
                .requests(List.of())
                .requestsSend(List.of())
                .usersRecommended(List.of(recommendedFriendsResponse))
                .build();
    }


    @Test
    void searchRequestsCorrectly() {
        when(friendRequestRepository.findAllByRequestedId(user.getId())).thenReturn(Optional.of(List.of(friendRequests)));
        when(friendshipService.searchMutualFriends(user, user2)).thenReturn(0);
        when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));
        when(requestMapper.myUserResponse(user2, 0)).thenReturn(listRequestsResponse);

        var result = requestsService.searchRequestsReceived(user);
        var expected = List.of(listRequestsResponse);

        Assertions.assertEquals(expected, result);
    }

    @Test
    void searchRequestsCorrectlyInUserWithoutPrompts() {
        when(friendRequestRepository.findAllByRequestedId(user.getId())).thenReturn(Optional.of(List.of()));

        var result = requestsService.searchRequestsReceived(user);
        var expected = List.of();

        Assertions.assertEquals(expected, result);
    }

    @Test
    void getRequestsCorrectly() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(friendRequestRepository.findAllByRequestedId(user.getId())).thenReturn(Optional.of(List.of(friendRequests)));
        when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));
        when(requestMapper.myUserResponse(user2, 0)).thenReturn(listRequestsResponse);

        when(friendRequestRepository.findAllByRequesterId(user.getId())).thenReturn(Optional.of(List.of(friendRequests2)));
        when(userRepository.findById(user4.getId())).thenReturn(Optional.of(user4));
        when(requestMapper.myUserResponse(user4, 0)).thenReturn(listRequestsResponseUser4);

        when(friendshipService.listFriendship(user)).thenReturn(List.of(userResponse));
        when(userRepository.findAll(PageRequest.of(0, 10))).thenReturn(Page.empty());

        when(requestMapper.getRequests(List.of(userResponse), List.of(listRequestsResponse), List.of(listRequestsResponseUser4), List.of())).thenReturn(requestResponse);

        var result = requestsService.getRequests(user.getEmail());

        Assertions.assertEquals(requestResponse, result);
    }

    @Test
    void getRequestsCorrectlyWithUsersRecommended() {
        Page<User> page = new PageImpl<>(List.of(user2));

        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        when(friendRequestRepository.findAllByRequestedId(user.getId())).thenReturn(Optional.of(List.of()));
        when(friendRequestRepository.findAllByRequesterId(user.getId())).thenReturn(Optional.of(List.of()));


        when(friendshipService.listFriendship(user)).thenReturn(List.of());

        when(userRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);
        when(friendshipService.searchMutualFriends(user, user2)).thenReturn(0);
        when(userMapper.userToRecommendedFriend(user2, 0)).thenReturn(recommendedFriendsResponse);

        when(requestMapper.getRequests(List.of(), List.of(), List.of(), List.of(recommendedFriendsResponse))).thenReturn(requestResponse);

        var result = requestsService.getRequests(user.getEmail());

        Assertions.assertEquals(requestResponse, result);
    }
}
