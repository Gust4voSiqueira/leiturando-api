package br.com.leiturando.mapper;

import br.com.leiturando.controller.response.user.MyUserResponse;
import br.com.leiturando.controller.response.user.RankingResponse;
import br.com.leiturando.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyUserMapper {
    public MyUserResponse myUserDtoToResponse(User user) {
        return MyUserResponse.builder()
                .image(user.getImage())
                .email(user.getEmail())
                .name(user.getName())
                .level(user.getLevel())
                .breakthrough(user.getBreakthrough())
                .matches(user.getMatches())
                .wrong(user.getWrong())
                .correct(user.getCorrect())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public RankingResponse userToRankingResponse(List<String> fields, List<Integer> data) {
        return RankingResponse
                .builder()
                .labels(fields)
                .datasets(List.of(RankingResponse.Datasets
                        .builder()
                        .data(data)
                        .build()))
                .build();
    }
}

