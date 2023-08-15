package br.com.leiturando.mapper;

import br.com.leiturando.controller.request.RegisterUserRequest;
import br.com.leiturando.controller.response.RegisterUserResponse;
import br.com.leiturando.domain.Const;
import br.com.leiturando.entity.Role;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import br.com.leiturando.repository.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterUserMapperTest {
    @InjectMocks
    RegisterUserMapper registerUserMapper;

    @Mock
    RoleRepository roleRepository;

    RegisterUserRequest registerUserRequest;
    User user;
    Role role;

    @BeforeEach
    public void init() {
        user = UserTest.builderUser();
        registerUserRequest = RegisterUserRequest
                .builder()
                    .characterName(user.getImage())
                    .name(user.getName())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .confirmPassword(user.getPassword())
                .build();
        role = new Role(Const.ROLE_CLIENT);
    }

    @Test
    void setRequestToUser() {
        user = UserTest.builderUser();

        when(roleRepository.findByName(Const.ROLE_CLIENT)).thenReturn(role);

        User result = registerUserMapper.requestToUser(registerUserRequest, registerUserRequest.getCharacterName(), registerUserRequest.getPassword());
        User expected = User
                .builder()
                    .name(registerUserRequest.getName())
                    .email(registerUserRequest.getEmail())
                    .password(registerUserRequest.getPassword())
                    .image(registerUserRequest.getCharacterName())
                    .level(1)
                    .breakthrough(0)
                    .friendships(List.of())
                    .roles(List.of(new Role(Const.ROLE_CLIENT)))
                .build();

        Assertions.assertEquals(expected.getName(), result.getName());
        Assertions.assertEquals(expected.getImage(), result.getImage());
        Assertions.assertEquals(expected.getLevel(), result.getLevel());
        Assertions.assertEquals(expected.getBreakthrough(), result.getBreakthrough());
        Assertions.assertEquals(expected.getPassword(), result.getPassword());
        Assertions.assertNotNull(expected.getFriendships());
        Assertions.assertNotNull(expected.getRoles());
    }

    @Test
    void setUserToRequest() {
        user = UserTest.builderUser();

        RegisterUserResponse result = registerUserMapper.userToRequest(user);
        RegisterUserResponse expected = RegisterUserResponse
                .builder()
                .image(user.getImage())
                .name(user.getName())
                .email(user.getEmail())
                .build();

        Assertions.assertEquals(expected.getName(), result.getName());
        Assertions.assertEquals(expected.getImage(), result.getImage());
        Assertions.assertEquals(expected.getEmail(), result.getEmail());
    }
}
