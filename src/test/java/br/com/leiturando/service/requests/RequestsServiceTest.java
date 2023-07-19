package br.com.leiturando.service.requests;

import br.com.leiturando.controller.response.*;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import br.com.leiturando.mapper.RequestMapper;
import br.com.leiturando.mapper.UserMapper;
import br.com.leiturando.repository.UserRepository;
import br.com.leiturando.service.FileService;
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

import java.util.List;

import static br.com.leiturando.Consts.PASSWORD_DEFAULT;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestsServiceTest {
    @InjectMocks
    RequestsService requestsService;

    @Mock
    SendRequestsService sendRequestsService;
    @Mock
    FriendshipService friendshipService;
    @Mock
    UserRepository userRepository;
    @Mock
    RequestMapper requestMapper;
    @Mock
    UserMapper userMapper;
    @Mock
    FileService fileService;

    User user;
    User user2;
    User user3;
    String image;
    ListRequestsResponse requestsResponse;
    List<ListRequestsResponse> requestsOfMyUser;
    List<UserResponse> friendshipUser;
    RecommendedFriendsResponse recommendedFriendsResponse2;
    MyUserResponse myUserResponse;
    RequestResponse requestResponseMyUser;

    @BeforeEach
    public void init() {
        user = UserTest.builderUser();
        user2 = User.builder()
                .id(2L)
                .name("Brena")
                .email("brena@gmail.com")
                .password(PASSWORD_DEFAULT)
                .imageUrl("batman")
                .friendships(List.of())
                .build();
        user3 = User.builder()
                .id(3L)
                .name("Marcelo")
                .email("marcelo@gmail.com")
                .password(PASSWORD_DEFAULT)
                .imageUrl("harry")
                .friendships(List.of())
                .build();
        image = "image";
        recommendedFriendsResponse2 = RecommendedFriendsResponse
                .builder()
                .id(user3.getId())
                .name(user3.getName())
                .urlImage(user3.getImageUrl())
                .mutualFriends(1)
                .build();
        requestsResponse = ListRequestsResponse
                .builder()
                .id(4L)
                .name("Rogério")
                .urlImage("batman")
                .mutualFriends(1)
                .build();
        requestsOfMyUser = List.of(requestsResponse);
        myUserResponse = MyUserResponse
                .builder()
                .imageUrl(user.getImageUrl())
                .name(user.getName())
                .level(user.getLevel())
                .breakthrough(user.getBreakthrough())
                .build();
        friendshipUser = List.of(
                UserResponse
                        .builder()
                            .id(user2.getId())
                            .name(user2.getName())
                            .urlImage(user2.getImageUrl())
                        .build()
        );
        requestResponseMyUser = RequestResponse
                .builder()
                .friends(friendshipUser)
                .requests(requestsOfMyUser)
                .usersRecommended(List.of(recommendedFriendsResponse2))
                .build();
    }

    @Test
    void myUserServiceTest() {
        PageRequest firstPageWithTwoElements = PageRequest.of(0, 10);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(sendRequestsService.searchRequestsReceived(user)).thenReturn(requestsOfMyUser);
        when(friendshipService.listFriendship(user)).thenReturn(friendshipUser);
        when(userRepository.findAll(firstPageWithTwoElements)).thenReturn(new PageImpl<>(List.of(user2, user3)));
        when(sendRequestsService.searchMutualFriends(user, user3)).thenReturn(1);
        when(requestMapper.getRequests(friendshipUser, requestsOfMyUser, List.of(), List.of(recommendedFriendsResponse2))).thenReturn(requestResponseMyUser);
        when(userMapper.userToRecommendedFriend(user3, user3.getImageUrl(), 1)).thenReturn(recommendedFriendsResponse2);
        when(fileService.downloadFile(user3.getImageUrl() + ".png")).thenReturn(user3.getImageUrl());


        var result = requestsService.getRequests(user.getEmail());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getFriends(), friendshipUser);
        Assertions.assertEquals(result.getRequests(), requestsOfMyUser);
        Assertions.assertEquals(result.getUsersRecommended(), List.of(recommendedFriendsResponse2));
    }
}
