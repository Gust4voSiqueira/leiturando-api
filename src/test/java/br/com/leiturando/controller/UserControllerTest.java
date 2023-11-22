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
    void getGlobalRanking() {
        when(myUserService.getGlobalRanking()).thenReturn(rankingResponse);

        var result = userController.getGlobalRanking();

        Assertions.assertEquals(rankingResponse, result);
    }
}
