package br.com.leiturando.controller;

import br.com.leiturando.BaseAuthTest;
import br.com.leiturando.controller.request.user.RegisterUserRequest;
import br.com.leiturando.controller.request.user.RegisterUserRequestTest;
import br.com.leiturando.controller.response.user.RankingResponse;
import br.com.leiturando.controller.response.user.RankingResponseTest;
import br.com.leiturando.controller.response.user.SearchUserResponse;
import br.com.leiturando.controller.response.user.SearchUserResponseTest;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import br.com.leiturando.service.FriendshipService;
import br.com.leiturando.service.MyUserService;
import br.com.leiturando.service.RegisterUserService;
import com.amazonaws.services.pinpoint.model.BadRequestException;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest extends BaseAuthTest {
    @InjectMocks
    UserController userController;

    @Mock
    RegisterUserService registerUserService;

    @Mock
    FriendshipService friendshipService;

    @Mock
    MyUserService myUserService;

    User user;
    User user2;
    List<User> users;
    SearchUserResponse searchUserResponse;
    RegisterUserRequest userRequestBatman;
    RegisterUserRequest registerUserRequest;
    RegisterUserRequest userRequestWithoutCharacter;
    MultipartFile file;
    RankingResponse rankingResponse;

    @BeforeEach
    void init() {
        user = UserTest.builderUser();
        user2 = User
                .builder()
                .id(2L)
                .name("Brena")
                .build();
        users = Collections.singletonList(user);
        userRequestBatman = RegisterUserRequest
                .builder()
                .characterName("Batman")
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .confirmPassword(user.getPassword())
                .build();
        userRequestWithoutCharacter = RegisterUserRequest
                .builder()
                .name(user.getName())
                .email(user.getEmail())
                .characterName(null)
                .password(user.getPassword())
                .confirmPassword(user.getPassword())
                .build();
        file = new MockMultipartFile("perfil", "../utils/perfil.jpeg", MediaType.IMAGE_JPEG_VALUE, "ImageProfile".getBytes());
        searchUserResponse = SearchUserResponseTest.builderSearchUserResponse();
        registerUserRequest = RegisterUserRequestTest.builderUserRequest();
        rankingResponse = RankingResponseTest.builderRankingResponse();
    }

    @Test
    void failedToRegisterUserAlreadyExists() {
        when(registerUserService.registerService(registerUserRequest)).thenReturn(new ResponseEntity<>("Já existe um usuário com este e-mail.", HttpStatus.CONFLICT));

        var result = userController.registerUser(registerUserRequest);
        var expected = new ResponseEntity<>("Já existe um usuário com este e-mail.", HttpStatus.CONFLICT);

        Assertions.assertEquals(expected, result);
    }

    @Test
    void registerUserSuccess() {
        when(registerUserService.registerService(userRequestBatman))
                .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        var result = userController.registerUser(userRequestBatman);
        var expected = new ResponseEntity<>(HttpStatus.CREATED);

        Assertions.assertEquals(result, expected);
    }

    @Test
    void failedToRegisterUserDuplicateEmail() {
        userRequestWithoutCharacter.setCharacterName(null);

        RegisterUserRequest registerUserRequest = RegisterUserRequest
                .builder()
                .characterName(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .confirmPassword(user.getPassword())
                .build();

        when(registerUserService.registerService(registerUserRequest))
                .thenReturn(new ResponseEntity<>("Já existe um usuário com este e-mail.", HttpStatus.CONFLICT));

        var result = userController.registerUser(registerUserRequest);
        var expected = new ResponseEntity<>("Já existe um usuário com este e-mail.", HttpStatus.CONFLICT);

        Assertions.assertEquals(result, expected);
    }

    @Test
    void failedToRegisterUserDifferencePasswords() {
        RegisterUserRequest registerUserRequest = RegisterUserRequest
                .builder()
                .characterName(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .confirmPassword("123")
                .build();

        when(registerUserService.registerService(registerUserRequest))
                .thenReturn(new ResponseEntity<>("As senhas são diferentes.", HttpStatus.BAD_REQUEST));

        var result = userController.registerUser(registerUserRequest);
        var expected = new ResponseEntity<>("As senhas são diferentes.", HttpStatus.BAD_REQUEST);

        Assertions.assertEquals(result, expected);
    }

    @Test
    void searchUsersCorrectly() {
        when(friendshipService.searchUsers("gusta", user.getEmail())).thenReturn(List.of(searchUserResponse));

        var result = userController.searchUsers("gusta");
        var expected =  List.of(searchUserResponse);

        Assertions.assertEquals(expected, result);
    }

    @Test
    void failedToSearchUsers() {
        when(friendshipService.searchUsers("gusta", user.getEmail())).thenThrow(BadRequestException.class);

        Assert.assertThrows(BadRequestException.class, () -> userController.searchUsers("gusta"));
    }

    @Test
    void unfriendCorrectly() {
        when(friendshipService.unFriend(user.getEmail(), user2.getId())).thenReturn(ResponseEntity.ok().body("Amizade desfeita."));

        var result = userController.unfriend(user2.getId());

        Assertions.assertEquals(result, ResponseEntity.ok().body("Amizade desfeita."));
    }

    @Test
    void failedToUnfriend() {
        when(friendshipService.unFriend(user.getEmail(), user2.getId())).thenThrow(BadRequestException.class);

        Assert.assertThrows(BadRequestException.class, () -> userController.unfriend(user2.getId()));
    }

    @Test
    void getGlobalRanking() {
        when(myUserService.getGlobalRanking()).thenReturn(rankingResponse);

        var result = userController.getGlobalRanking();

        Assertions.assertEquals(rankingResponse, result);
    }

    @Test
    void failedToGetGlobalRanking() {
        when(myUserService.getGlobalRanking()).thenThrow(BadRequestException.class);

        Assert.assertThrows(BadRequestException.class, () -> userController.getGlobalRanking());
    }

    @Test
    void getFriendsRanking() {
        when(myUserService.getFriendsRanking(user.getEmail())).thenReturn(rankingResponse);

        var result = userController.getFriendsRanking();

        Assertions.assertEquals(rankingResponse, result);
    }

    @Test
    void failedToGetFriendsRanking() {
        when(myUserService.getFriendsRanking(user.getEmail())).thenThrow(BadRequestException.class);

        Assert.assertThrows(BadRequestException.class, () -> userController.getFriendsRanking());
    }
}
