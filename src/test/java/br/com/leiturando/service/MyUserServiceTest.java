package br.com.leiturando.service;


import br.com.leiturando.controller.response.ListRequestsResponse;
import br.com.leiturando.controller.response.MyUserResponse;
import br.com.leiturando.controller.response.RecommendedFriendsResponse;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import br.com.leiturando.mapper.MyUserMapper;
import br.com.leiturando.mapper.UserMapper;
import br.com.leiturando.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static br.com.leiturando.Consts.PASSWORD_DEFAULT;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class MyUserServiceTest {
    @InjectMocks
    MyUserService myUserService;

    @Mock
    MyUserMapper myUserMapper;

    @Mock
    UserMapper userMapper;

    @Mock
    UserRepository userRepository;

    @Mock
    RequestsService requestsService;

    @Mock
    FriendshipService friendshipService;

    User user;
    User user2;
    User user3;
    ListRequestsResponse requestsResponse;
    List<ListRequestsResponse> requestsOfMyUser;
    RecommendedFriendsResponse recommendedFriendsResponse1;
    RecommendedFriendsResponse recommendedFriendsResponse2;
    MyUserResponse myUserResponse;

    @Before
    public void init() {
        user = UserTest.builderUser();
        user2 = User.builder()
                .id(2L)
                .name("Brena")
                .email("brena@gmail.com")
                .password(PASSWORD_DEFAULT)
                .imageUrl("Batman")
                .friendships(List.of())
                .build();
        user3 = User.builder()
                .id(3L)
                .name("Marcelo")
                .email("marcelo@gmail.com")
                .password(PASSWORD_DEFAULT)
                .imageUrl("Harry")
                .friendships(List.of())
                .build();
        recommendedFriendsResponse1 = RecommendedFriendsResponse
                .builder()
                .id(user2.getId())
                .name(user2.getName())
                .urlImage(user2.getImageUrl())
                .mutualFriends(1)
                .build();
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
                .urlImage("Flash")
                .mutualFriends(1)
                .build();
        requestsOfMyUser = List.of(requestsResponse);
        myUserResponse = MyUserResponse
                .builder()
                .imageUrl(user.getImageUrl())
                .name(user.getName())
                .level(user.getLevel())
                .breakthrough(user.getBreakthrough())
                .friends(List.of())
                .requests(List.of(requestsResponse))
                .usersRecomended(List.of(recommendedFriendsResponse1, recommendedFriendsResponse2))
                .build();
    }

    @Test
    public void myUserServiceTest() {
        PageRequest firstPageWithTwoElements = PageRequest.of(0, 10);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(requestsService.searchRequests(user)).thenReturn(requestsOfMyUser);
        when(friendshipService.listFriendship(user)).thenReturn(List.of());
        when(userRepository.findAll(firstPageWithTwoElements)).thenReturn(new PageImpl<>(List.of(user2, user3)));

        when(requestsService.searchMutualFriends(user, user2)).thenReturn(1);
        when(requestsService.searchMutualFriends(user, user3)).thenReturn(1);
        when(userMapper.userToRecommendedFriend(user2, 1)).thenReturn(recommendedFriendsResponse1);
        when(userMapper.userToRecommendedFriend(user3, 1)).thenReturn(recommendedFriendsResponse2);
        when(myUserMapper.myUserDtoToResponse(user, List.of(), requestsOfMyUser, List.of(recommendedFriendsResponse1, recommendedFriendsResponse2))).thenReturn(myUserResponse);

        var result = myUserService.myUserService(user.getEmail());

        assertEquals(myUserResponse.getName(), result.getName());
        assertEquals(myUserResponse.getRequests(), result.getRequests());
        assertEquals(myUserResponse.getUsersRecomended(), result.getUsersRecomended());
        assertEquals(myUserResponse.getFriends(), result.getFriends());
    }
}