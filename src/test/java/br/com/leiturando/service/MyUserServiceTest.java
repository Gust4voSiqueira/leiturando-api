package br.com.leiturando.service;

import br.com.leiturando.controller.response.ListRequestsResponse;
import br.com.leiturando.controller.response.MyUserResponse;
import br.com.leiturando.controller.response.RecommendedFriendsResponse;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import br.com.leiturando.mapper.MyUserMapper;
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

import static br.com.leiturando.Consts.PASSWORD_DEFAULT;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyUserServiceTest {
    @InjectMocks
    MyUserService myUserService;

    @Mock
    MyUserMapper myUserMapper;

    @Mock
    UserRepository userRepository;

    @Mock
    FileService fileService;

    User user;
    User user2;
    User user3;
    ListRequestsResponse requestsResponse;
    List<ListRequestsResponse> requestsOfMyUser;
    RecommendedFriendsResponse recommendedFriendsResponse1;
    RecommendedFriendsResponse recommendedFriendsResponse2;
    MyUserResponse myUserResponse;

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
                .urlImage("thor")
                .mutualFriends(1)
                .build();
        requestsOfMyUser = List.of(requestsResponse);
        myUserResponse = MyUserResponse
                .builder()
                .urlImage(user.getImageUrl())
                .name(user.getName())
                .level(user.getLevel())
                .breakthrough(user.getBreakthrough())
                .build();
    }

    @Test
    void myUserServiceTest() throws IOException {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(myUserMapper.myUserDtoToResponse(user, user.getImageUrl())).thenReturn(myUserResponse);
        when(fileService.downloadFile(user.getImageUrl())).thenReturn(user.getImageUrl());

        var result = myUserService.myUserService(user.getEmail());

        Assertions.assertEquals(myUserResponse.getName(), result.getName());
    }
}
