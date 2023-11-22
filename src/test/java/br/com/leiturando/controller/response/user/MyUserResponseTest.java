package br.com.leiturando.controller.response.user;

import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;


public class MyUserResponseTest {
    public static MyUserResponse builderMyUserResponse() {
        User user = UserTest.builderUser();

        return MyUserResponse
                .builder()
                .image(UserTest.builderUser().getImage())
                .email(UserTest.builderUser().getEmail())
                .name(UserTest.builderUser().getName())
                .level(1)
                .breakthrough(0)
                .createdAt(LocalDateTime.now())
                .matches(user.getMatches())
                .correct(user.getCorrect())
                .wrong(user.getWrong())
                .build();
    }

    @Test
    void buildMyUserResponseCorrectly() {
        var myUserResponse = builderMyUserResponse();

        Assertions.assertNotNull(myUserResponse.getImage());
        Assertions.assertNotNull(myUserResponse.getName());
        Assertions.assertNotNull(myUserResponse.getEmail());
        Assertions.assertNotNull(myUserResponse.getLevel());
        Assertions.assertNotNull(myUserResponse.getBreakthrough());
        Assertions.assertNotNull(myUserResponse.getCreatedAt());
        Assertions.assertNotNull(myUserResponse.getMatches());
        Assertions.assertNotNull(myUserResponse.getCorrect());
        Assertions.assertNotNull(myUserResponse.getWrong());
    }
}
