package br.com.leiturando.mapper;


import br.com.leiturando.controller.response.*;
import br.com.leiturando.entity.FriendRequests;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static br.com.leiturando.Consts.PASSWORD_DEFAULT;

@ExtendWith(MockitoExtension.class)
class RequestMapperTest {
    @InjectMocks
    RequestMapper requestMapper;

    FriendRequests friendRequests;
    User user1;
    User user2;
    List<UserResponse> friends;
    List<ListRequestsResponse> requests;
    List<RecommendedFriendsResponse> usersRecommended;

    @BeforeEach
    public void init() {
        user1 = UserTest.builderUser();
        user2 = User
                .builder()
                    .id(2L)
                    .name("rogerio")
                    .email("rogerio@gmail.com")
                    .password(PASSWORD_DEFAULT)
                    .imageUrl("Batman")
                    .friendships(List.of())
                .build();

        friendRequests = FriendRequests
                .builder()
                    .requester(user1)
                    .requested(user2)
                .build();
        friends = List.of(UserResponse
                .builder()
                    .id(user1.getId())
                    .name(user1.getName())
                    .urlImage(user1.getImageUrl())
                .build()
        );
        requests = List.of(ListRequestsResponse
                .builder()
                    .name(user2.getName())
                    .urlImage(user2.getImageUrl())
                    .mutualFriends(1)
                .build()
        );
        usersRecommended = List.of(RecommendedFriendsResponse
                .builder()
                        .id(3L)
                        .urlImage("spiderman")
                        .name("Fred")
                        .mutualFriends(1)
                .build()
        );
    }

    @Test
    void setCreateRequest(){
        FriendRequests result = requestMapper.createRequest(user1, user2);
        FriendRequests expected = FriendRequests
                .builder()
                .requester(user1)
                .requested(user2)
                .build();

        Assertions.assertEquals(expected.getRequester(), result.getRequester());
        Assertions.assertEquals(expected.getRequested(), result.getRequested());
    }

    @Test
    void setRequestToResponse() {
        SendRequestResponse result = requestMapper.requestToResponse(friendRequests);

        Assertions.assertEquals(user1.getId(), result.getRequesterId());
        Assertions.assertEquals(user2.getId(), result.getRequestedId());
    }

    @Test
    void setMyUserResponse() {
        ListRequestsResponse result = requestMapper.myUserResponse(user1, user1.getImageUrl(), 1);
        ListRequestsResponse expected = ListRequestsResponse
                .builder()
                    .id(user1.getId())
                    .name(user1.getName())
                    .urlImage(user1.getImageUrl())
                    .mutualFriends(1)
                .build();

        Assertions.assertEquals(expected.getId(), result.getId());
        Assertions.assertEquals(expected.getName(), result.getName());
        Assertions.assertEquals(expected.getUrlImage(), result.getUrlImage());
        Assertions.assertEquals(expected.getMutualFriends(), result.getMutualFriends());
    }

    @Test
    void getRequests() {
        RequestResponse result = requestMapper.getRequests(friends, requests, List.of(), usersRecommended);
        RequestResponse expected = RequestResponse
                .builder()
                    .friends(friends)
                    .requests(requests)
                    .requestsSend(List.of())
                    .usersRecommended(usersRecommended)
                .build();

        Assertions.assertEquals(expected.getFriends(), result.getFriends());
        Assertions.assertEquals(expected.getRequests(), result.getRequests());
        Assertions.assertEquals(expected.getRequestsSend(), result.getRequestsSend());
        Assertions.assertEquals(expected.getUsersRecommended(), result.getUsersRecommended());
    }
}
