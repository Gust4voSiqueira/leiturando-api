package br.com.leiturando.mapper;

import br.com.leiturando.controller.response.MyUserResponse;
import br.com.leiturando.entity.User;
import org.springframework.stereotype.Component;

@Component
public class MyUserMapper {
    public MyUserResponse myUserDtoToResponse(User user) {
        return MyUserResponse.builder()
                .image(user.getImage())
                .name(user.getName())
                .level(user.getLevel())
                .breakthrough(user.getBreakthrough())
                .matches(user.getMatches())
                .wrong(user.getWrong())
                .correct(user.getCorrect())
                .createdAt(user.getCreatedAt())
                .build();
    }
}

