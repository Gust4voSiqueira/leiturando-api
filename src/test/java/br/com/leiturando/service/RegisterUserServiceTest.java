package br.com.leiturando.service;

import br.com.leiturando.controller.request.RegisterUserRequest;
import br.com.leiturando.controller.response.RegisterUserResponse;
import br.com.leiturando.domain.Const;
import br.com.leiturando.entity.Role;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import br.com.leiturando.exception.UserExistsException;
import br.com.leiturando.mapper.RegisterUserMapper;
import br.com.leiturando.repository.UserRepository;
import com.amazonaws.services.ecr.model.ImageNotFoundException;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterUserServiceTest {
    @InjectMocks
    RegisterUserService registerUserService;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    FileService fileService;

    @Mock
    RegisterUserMapper registerUserMapper;

    User user;
    List<User> users;
    RegisterUserRequest registerUserRequest;
    RegisterUserResponse registerUserResponse;
    MultipartFile file;
    Role role;

    @BeforeEach
    void init() {
        user = UserTest.builderUser();
        users = Collections.singletonList(user);
        registerUserRequest = RegisterUserRequest
                .builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .confirmPassword(user.getPassword())
                .build();
        file = new MockMultipartFile("profile", "profile", MediaType.IMAGE_JPEG_VALUE, "ImageProfile".getBytes());
        role = new Role(Const.ROLE_CLIENT);
        registerUserResponse = RegisterUserResponse
                .builder()
                .name(registerUserRequest.getName())
                .email(registerUserRequest.getEmail())
                .build();
    }

    @Test
    void registerUserWithoutFileSuccess() throws Exception {
        registerUserResponse.setUrlImage("Batman");
        registerUserRequest.setCharacterName("Batman");

        when(userRepository.findByEmail(registerUserRequest.getEmail())).thenReturn(null);
        when(userRepository.save(user)).thenReturn(user);
        when(passwordEncoder.encode(registerUserRequest.getPassword())).thenReturn(registerUserRequest.getPassword());
        when(registerUserMapper.userToRequest(user)).thenReturn(registerUserResponse);
        when(registerUserMapper.requestToUser(
                registerUserRequest,
                registerUserRequest.getCharacterName(),
                passwordEncoder.encode(registerUserRequest.getPassword()))).thenReturn(user);

        var result = registerUserService.registerService(registerUserRequest);

        Assertions.assertEquals(registerUserResponse, result);
    }

    @Test
    void registerUserWithFileSuccess() throws Exception {
        registerUserResponse.setUrlImage(file.getName());
        registerUserRequest.setFile(file);

        String imageName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        when(userRepository.findByEmail(registerUserRequest.getEmail())).thenReturn(null);
        when(userRepository.save(user)).thenReturn(user);
        when(passwordEncoder.encode(registerUserRequest.getPassword())).thenReturn(registerUserRequest.getPassword());
        when(registerUserMapper.userToRequest(user)).thenReturn(registerUserResponse);
        when(fileService.uploadFile(file)).thenReturn(imageName);
        when(registerUserMapper.requestToUser(
                registerUserRequest,
                imageName,
                passwordEncoder.encode(registerUserRequest.getPassword()))).thenReturn(user);

        var result = registerUserService.registerService(registerUserRequest);

        Assertions.assertEquals(registerUserResponse, result);
    }

    @Test
    void failedToRegisterUserAlreadyRegistered() {
        when(userRepository.findByEmail(registerUserRequest.getEmail())).thenReturn(user);
        registerUserRequest.setFile(file);

        UserExistsException exception = Assertions.assertThrows(UserExistsException.class,
                () -> registerUserService.registerService(registerUserRequest));

        Assertions.assertEquals("Já existe um usuário com este e-mail.", exception.getLocalizedMessage());
    }

    @Test
    void failedToRegisterUserToPasswordDifferent() {
        registerUserRequest.setConfirmPassword("123");
        when(userRepository.findByEmail(registerUserRequest.getEmail())).thenReturn(null);
        registerUserRequest.setFile(file);

        PasswordException exception = Assertions.assertThrows(PasswordException.class,
                () -> registerUserService.registerService(registerUserRequest));

        Assertions.assertEquals("As senhas são diferentes.", exception.getLocalizedMessage());
    }

    @Test
    void failedToRegisterUserWithoutFileAndCharacter() {
        registerUserRequest.setCharacterName(null);
        registerUserRequest.setFile(null);
        when(userRepository.findByEmail(registerUserRequest.getEmail())).thenReturn(null);

        ImageNotFoundException exception = Assertions.assertThrows(ImageNotFoundException.class,
                () -> registerUserService.registerService(registerUserRequest));

        Assertions.assertEquals("Escolha uma imagem para o perfil.", exception.getErrorMessage());
    }
}
