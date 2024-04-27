package br.com.leiturando.service;

import br.com.leiturando.controller.request.user.UpdateUserRequest;
import br.com.leiturando.controller.request.user.UpdateUserRequestTest;
import br.com.leiturando.controller.response.requests.ListRequestsResponse;
import br.com.leiturando.controller.response.user.MyUserResponse;
import br.com.leiturando.controller.response.requests.RecommendedFriendsResponse;
import br.com.leiturando.controller.response.user.RankingResponse;
import br.com.leiturando.controller.response.user.RankingResponseTest;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import br.com.leiturando.mapper.MyUserMapper;
import br.com.leiturando.repository.UserRepository;
import com.amazonaws.services.pinpoint.model.BadRequestException;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    PasswordEncoder passwordEncoder;

    User user;
    User user2;
    User user3;
    ListRequestsResponse requestsResponse;
    List<ListRequestsResponse> requestsOfMyUser;
    RecommendedFriendsResponse recommendedFriendsResponse1;
    RecommendedFriendsResponse recommendedFriendsResponse2;
    MyUserResponse myUserResponse;
    UpdateUserRequest updateUserRequest;
    RankingResponse rankingResponse;

    @BeforeEach
    public void init() {
        user = UserTest.builderUser();
        user2 = User.builder()
                .id(2L)
                .name("Brena")
                .email("brena@gmail.com")
                .password(PASSWORD_DEFAULT)
                .image("batman")
                .level(10)
                .friendships(List.of())
                .build();
        user3 = User.builder()
                .id(3L)
                .name("Marcelo")
                .email("marcelo@gmail.com")
                .password(PASSWORD_DEFAULT)
                .image("harry")
                .level(8)
                .friendships(List.of())
                .build();
        recommendedFriendsResponse1 = RecommendedFriendsResponse
                .builder()
                .id(user2.getId())
                .name(user2.getName())
                .image(user2.getImage())
                .mutualFriends(1)
                .build();
        recommendedFriendsResponse2 = RecommendedFriendsResponse
                .builder()
                .id(user3.getId())
                .name(user3.getName())
                .image(user3.getImage())
                .mutualFriends(1)
                .build();
        requestsResponse = ListRequestsResponse
                .builder()
                .id(4L)
                .name("Rogério")
                .image("thor")
                .mutualFriends(1)
                .build();
        requestsOfMyUser = List.of(requestsResponse);
        myUserResponse = MyUserResponse
                .builder()
                .image(user.getImage())
                .name(user.getName())
                .level(user.getLevel())
                .breakthrough(user.getBreakthrough())
                .build();
        updateUserRequest = UpdateUserRequestTest.builderUpdateUserRequest();
        rankingResponse = RankingResponseTest.builderRankingResponse();
    }

    @Test
    void myUserServiceTest() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(myUserMapper.myUserDtoToResponse(user)).thenReturn(myUserResponse);

        var result = myUserService.myUserService(user.getEmail());

        Assertions.assertEquals(myUserResponse.getName(), result.getName());
    }

    @Test
    void editProfileCorrectly() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(myUserMapper.myUserDtoToResponse(user)).thenReturn(myUserResponse);
        when(passwordEncoder.encode(user.getPassword())).thenReturn(user.getPassword());

        var result = myUserService.editProfile(user.getEmail(), updateUserRequest);

        Assertions.assertEquals(ResponseEntity.ok().body(myUserResponse), result);
    }

    @Test
    void failedToEditProfilePasswordDifferent() {
        updateUserRequest.setConfirmNewPassword("12345");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        var email = user.getEmail();

        Assert.assertThrows(BadRequestException.class, () -> myUserService.editProfile(email, updateUserRequest));
    }

    @Test
    void getGlobalRankingCorrectly() {
        user.setName("Gusta");
        user3.setName("Marce");

        List<String> fields = Stream.of(user, user2, user3).map(User::getName).collect(Collectors.toList());
        List<Integer> data = Stream.of(user, user2, user3).map(User::getLevel).collect(Collectors.toList());


        when(userRepository.findFirst5ByOrderByLevelDesc()).thenReturn(List.of(user, user2, user3));
        when(myUserMapper.userToRankingResponse(fields, data)).thenReturn(rankingResponse);

        var result = myUserService.getGlobalRanking();

        Assertions.assertEquals(rankingResponse, result);
    }
}
