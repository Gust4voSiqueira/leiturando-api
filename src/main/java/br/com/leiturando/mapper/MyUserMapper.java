package br.com.leiturando.mapper;

import br.com.leiturando.controller.response.MyUserResponse;
import br.com.leiturando.entity.User;
import org.springframework.stereotype.Component;

@Component
public class MyUserMapper {
    public MyUserResponse myUserDtoToResponse(User user, String image) {
        return new MyUserResponse(
                image,
                user.getName(),
                user.getLevel(),
                user.getBreakthrough()
        );
    }
}

