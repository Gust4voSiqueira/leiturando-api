package br.com.leiturando.service;

import br.com.leiturando.controller.request.RegisterUserRequest;
import br.com.leiturando.controller.response.RegisterUserResponse;
import br.com.leiturando.domain.Const;
import br.com.leiturando.entity.Role;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import br.com.leiturando.exception.UserExistsException;
import br.com.leiturando.mapper.RegisterUserMapper;
import br.com.leiturando.repository.RoleRepository;
import br.com.leiturando.repository.UserRepository;
import com.amazonaws.services.ecr.model.ImageNotFoundException;
import org.bouncycastle.openssl.PasswordException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class RegisterUserServiceTest {
    @InjectMocks
    RegisterUserService registerUserService;

    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

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

    @Before
    public void init() {
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
    public void registerUserWithoutFileSuccess() throws Exception {
        registerUserResponse.setUrlImage("Batman");
        registerUserRequest.setCharacterName("Batman");

        when(userRepository.findByEmail(registerUserRequest.getEmail())).thenReturn(null);
        when(userRepository.save(user)).thenReturn(user);
        when(roleRepository.save(role)).thenReturn(role);
        when(passwordEncoder.encode(registerUserRequest.getPassword())).thenReturn(registerUserRequest.getPassword());
        when(registerUserMapper.userToRequest(user)).thenReturn(registerUserResponse);
        when(registerUserMapper.requestToUser(
                registerUserRequest,
                registerUserRequest.getCharacterName(),
                passwordEncoder.encode(registerUserRequest.getPassword()))).thenReturn(user);

        var result = registerUserService.registerService(registerUserRequest, null);

        assertEquals(registerUserResponse, result);
    }

    @Test
    public void registerUserWithFileSuccess() throws Exception {
        registerUserResponse.setUrlImage(file.getName());

        String imageName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        when(userRepository.findByEmail(registerUserRequest.getEmail())).thenReturn(null);
        when(userRepository.save(user)).thenReturn(user);
        when(roleRepository.save(role)).thenReturn(role);
        when(passwordEncoder.encode(registerUserRequest.getPassword())).thenReturn(registerUserRequest.getPassword());
        when(registerUserMapper.userToRequest(user)).thenReturn(registerUserResponse);
        when(fileService.uploadFile(file)).thenReturn(imageName);
        when(registerUserMapper.requestToUser(
                registerUserRequest,
                imageName,
                passwordEncoder.encode(registerUserRequest.getPassword()))).thenReturn(user);

        var result = registerUserService.registerService(registerUserRequest, file);

        assertEquals(registerUserResponse, result);
    }

    @Test(expected = UserExistsException.class)
    public void failedToRegisterUserAlreadyRegistered() throws Exception {
        when(userRepository.findByEmail(registerUserRequest.getEmail())).thenReturn(user);

        registerUserService.registerService(registerUserRequest, file);
    }

    @Test(expected = PasswordException.class)
    public void failedToRegisterUserToPasswordDifferent() throws Exception {
        registerUserRequest.setConfirmPassword("123");
        when(userRepository.findByEmail(registerUserRequest.getEmail())).thenReturn(null);

        registerUserService.registerService(registerUserRequest, file);
    }

    @Test(expected = ImageNotFoundException.class)
    public void failedToRegisterUserWithoutFileAndCharacter() throws Exception {
        registerUserRequest.setCharacterName(null);
        when(userRepository.findByEmail(registerUserRequest.getEmail())).thenReturn(null);

        registerUserService.registerService(registerUserRequest, null);
    }
}
