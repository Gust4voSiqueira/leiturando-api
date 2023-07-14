package br.com.leiturando.mapper;

import br.com.leiturando.controller.request.RegisterUserRequest;
import br.com.leiturando.controller.response.RegisterUserResponse;
import br.com.leiturando.domain.Const;
import br.com.leiturando.entity.Role;
import br.com.leiturando.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RegisterUserMapper {
    public User requestToUser(RegisterUserRequest userRequest, String urlImage, String password) {
        Role role = new Role(Const.ROLE_CLIENT);
        return new User(
                userRequest.getName(),
                userRequest.getEmail(),
                urlImage,
                1,
                0,
                password,
                List.of(role)
        );
    }

    public RegisterUserResponse userToRequest(User user) {
        return new RegisterUserResponse(
                user.getImageUrl(),
                user.getName(),
                user.getEmail()
        );
    }
}