package br.com.leiturando.service;

import br.com.leiturando.controller.request.user.RegisterUserRequest;
import br.com.leiturando.domain.Const;
import br.com.leiturando.entity.Role;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import br.com.leiturando.mapper.RegisterUserMapper;
import br.com.leiturando.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    RegisterUserMapper registerUserMapper;

    User user;
    List<User> users;
    RegisterUserRequest registerUserRequest;
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
        role = new Role(Const.ROLE_CLIENT);
    }

    @Test
    void registerUserSuccess() {
        registerUserRequest.setCharacterName("Batman");

        when(userRepository.findByEmail(registerUserRequest.getEmail())).thenReturn(null);
        when(userRepository.save(user)).thenReturn(user);
        when(passwordEncoder.encode(registerUserRequest.getPassword())).thenReturn(registerUserRequest.getPassword());
        when(registerUserMapper.requestToUser(
                registerUserRequest,
                registerUserRequest.getCharacterName(),
                passwordEncoder.encode(registerUserRequest.getPassword()))).thenReturn(user);

        var result = registerUserService.registerService(registerUserRequest);
        var expected = new ResponseEntity<>(HttpStatus.CREATED);

        Assertions.assertEquals(expected, result);
    }

    @Test
    void failedToRegisterUserAlreadyRegistered() {
        when(userRepository.findByEmail(registerUserRequest.getEmail())).thenReturn(user);

        var result = registerUserService.registerService(registerUserRequest);
        var expected = new ResponseEntity<>("Já existe um usuário com este e-mail.", HttpStatus.CONFLICT);

        Assertions.assertEquals(result, expected);
    }
}
