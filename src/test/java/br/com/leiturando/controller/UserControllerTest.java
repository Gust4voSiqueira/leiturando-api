package br.com.leiturando.controller;

import br.com.leiturando.BaseAuthTest;
import br.com.leiturando.controller.request.RegisterUserRequest;
import br.com.leiturando.controller.response.RegisterUserResponse;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import br.com.leiturando.exception.UserExistsException;
import br.com.leiturando.service.RegisterUserService;
import com.amazonaws.services.ecr.model.ImageNotFoundException;
import com.amazonaws.services.pinpoint.model.BadRequestException;
import com.amazonaws.services.pinpoint.model.InternalServerErrorException;
import org.bouncycastle.openssl.PasswordException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
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

    User user;
    List<User> users;
    RegisterUserRequest userRequestBatman;
    RegisterUserResponse registerUserBatmanResponse;
    RegisterUserRequest userRequestWithoutCharacter;
    RegisterUserResponse registerUserWithoutCharacterResponse;
    MultipartFile file;

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
    void registerUserWithoutFileSuccess() throws Exception {
        when(registerUserService.registerService(userRequestBatman))
                .thenReturn(registerUserBatmanResponse);

        var result = userController.registerUser(userRequestBatman);
        var expectedResult = registerUserBatmanResponse;

        Assertions.assertEquals(result, expectedResult);
    }

    @Test
    void registerUserWithFileSuccess() throws Exception {
        userRequestWithoutCharacter.setFile(file);
        when(registerUserService.registerService(userRequestWithoutCharacter))
                .thenReturn(registerUserWithoutCharacterResponse);

        var result = userController.registerUser(userRequestWithoutCharacter);
        var expectedResult = registerUserWithoutCharacterResponse;

        Assertions.assertEquals(result, expectedResult);
    }

    @Test
    void failedToRegisterUserWithoutFileAndCharacterName() throws Exception {
        userRequestWithoutCharacter.setFile(null);
        userRequestWithoutCharacter.setCharacterName(null);

        RegisterUserRequest registerUserRequest = RegisterUserRequest
                .builder()
                .characterName(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .confirmPassword(user.getPassword())
                .build();

        when(registerUserService.registerService(registerUserRequest))
                .thenThrow(new ImageNotFoundException("Escolha uma imagem para o perfil."));

        Exception exception = Assertions.assertThrows(BadRequestException.class,
                () -> userController.registerUser(registerUserRequest));

        Assertions.assertNotNull(exception.getLocalizedMessage());
    }

    @Test
    void failedToRegisterUserDuplicateEmail() throws Exception {
        userRequestWithoutCharacter.setFile(null);
        userRequestWithoutCharacter.setCharacterName(null);

        RegisterUserRequest registerUserRequest = RegisterUserRequest
                .builder()
                .characterName(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .confirmPassword(user.getPassword())
                .build();

        when(registerUserService.registerService(registerUserRequest))
                .thenThrow(new UserExistsException("Já existe um usuário com este e-mail."));

        Exception exception = Assertions.assertThrows(BadRequestException.class,
                () -> userController.registerUser(registerUserRequest));

        Assertions.assertNotNull(exception.getMessage());
    }

    @Test
    void failedToRegisterUserDifferencePasswords() throws Exception {
        userRequestWithoutCharacter.setFile(null);
        userRequestWithoutCharacter.setCharacterName(null);

        RegisterUserRequest registerUserRequest = RegisterUserRequest
                .builder()
                .characterName(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .confirmPassword("123")
                .build();

        when(registerUserService.registerService(registerUserRequest))
                .thenThrow(new PasswordException("As senhas são diferentes."));

        Exception exception = Assertions.assertThrows(InternalServerErrorException.class,
                () -> userController.registerUser(registerUserRequest));

        Assertions.assertNotNull(exception.getMessage());
    }
}
