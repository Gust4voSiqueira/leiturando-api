package br.com.leiturando.mapper;

import br.com.leiturando.controller.request.RegisterUserRequest;
import br.com.leiturando.controller.response.RegisterUserResponse;
import br.com.leiturando.domain.Const;
import br.com.leiturando.entity.Role;
import br.com.leiturando.entity.User;
import br.com.leiturando.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class RegisterUserMapper {
    @Autowired
    RoleRepository roleRepository;

    public User requestToUser(RegisterUserRequest userRequest, String image, String password) {
        Role role = roleRepository.findByName(Const.ROLE_CLIENT);

        return User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .password(password)
                .image(image)
                .dateOfBirth(userRequest.getDateOfBirth())
                .level(1)
                .breakthrough(0)
                .roles(List.of(role))
                .wrong(0)
                .correct(0)
                .matches(0)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public RegisterUserResponse userToRequest(User user) {
        return new RegisterUserResponse(
                user.getImage(),
                user.getName(),
                user.getEmail()
        );
    }
}
