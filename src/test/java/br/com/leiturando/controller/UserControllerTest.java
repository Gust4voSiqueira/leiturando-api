package br.com.leiturando.controller;

import br.com.leiturando.BaseAuthTest;
import br.com.leiturando.controller.request.RegisterUserRequest;
import br.com.leiturando.controller.response.RegisterUserResponse;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import br.com.leiturando.exception.UserExistsException;
import br.com.leiturando.service.RegisterUserService;
import com.amazonaws.services.ecr.model.ImageNotFoundException;
import org.bouncycastle.openssl.PasswordException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class UserControllerTest extends BaseAuthTest {
    @InjectMocks
    UserController userController;

    @Mock
    RegisterUserService registerUserService;

    User user;
    List<User> users;
    RegisterUserRequest userRequestBatman;
    RegisterUserResponse registerUserBatmanResponse;
    RegisterUserRequest userRequestWithoutCharacter;
    RegisterUserResponse registerUserWithoutCharacterResponse;
    MultipartFile file;

    @Before
    public void init() {
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
        registerUserBatmanResponse = RegisterUserResponse
                .builder()
                .urlImage(userRequestBatman.getCharacterName())
                .name(userRequestBatman.getName())
                .email(userRequestBatman.getEmail())
                .build();
        registerUserWithoutCharacterResponse = RegisterUserResponse
                .builder()
                .urlImage(file.getName())
                .name(userRequestWithoutCharacter.getName())
                .email(userRequestWithoutCharacter.getEmail())
                .build();
    }

    @Test
    public void registerUserWithoutFileSuccess() throws Exception {
        when(registerUserService.registerService(userRequestBatman, null))
                .thenReturn(registerUserBatmanResponse);

        var result = userController.registerUser(userRequestBatman, null);
        var expectedResult = registerUserBatmanResponse;

        assertEquals(result, expectedResult);
    }

    @Test
    public void registerUserWithFileSuccess() throws Exception {
        when(registerUserService.registerService(userRequestWithoutCharacter, file))
                .thenReturn(registerUserWithoutCharacterResponse);

        var result = userController.registerUser(userRequestWithoutCharacter, file);
        var expectedResult = registerUserWithoutCharacterResponse;

        assertEquals(result, expectedResult);
    }

    @Test(expected = ImageNotFoundException.class)
    public void failedToRegisterUserWithoutFileAndCharacterName() throws Exception {
        RegisterUserRequest registerUserRequest = RegisterUserRequest
                .builder()
                .characterName(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .confirmPassword(user.getPassword())
                .build();

        when(registerUserService.registerService(registerUserRequest, null))
                .thenThrow(new ImageNotFoundException("Escolha uma imagem para o perfil."));

        registerUserService.registerService(registerUserRequest, null);
    }

    @Test(expected = UserExistsException.class)
    public void failedToRegisterUserDuplicateEmail() throws Exception {
        RegisterUserRequest registerUserRequest = RegisterUserRequest
                .builder()
                .characterName(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .confirmPassword(user.getPassword())
                .build();

        when(registerUserService.registerService(registerUserRequest, null))
                .thenThrow(new UserExistsException("Já existe um usuário com este e-mail."));

        registerUserService.registerService(registerUserRequest, null);
    }

    @Test(expected = PasswordException.class)
    public void failedToRegisterUserDifferencePasswords() throws Exception {
        RegisterUserRequest registerUserRequest = RegisterUserRequest
                .builder()
                .characterName(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .confirmPassword("123")
                .build();

        when(registerUserService.registerService(registerUserRequest, null))
                .thenThrow(new PasswordException("As senhas são diferentes."));

        registerUserService.registerService(registerUserRequest, null);
    }
}
