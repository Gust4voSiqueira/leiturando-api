package br.com.leiturando.controller.response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class MyUserResponseTest {
    public static MyUserResponse builderMyUserResponse() {
        return MyUserResponse
                .builder()
                .imageUrl("Batman")
                .name("Gustavo")
                .level(1)
                .breakthrough(0)
                .friends(List.of())
                .requests(List.of())
                .usersRecomended(List.of())
                .build();
    }

    @Test
    void buildMyUserResponseCorrectly() {
        var myUserResponse = builderMyUserResponse();

        Assertions.assertNotNull(myUserResponse.getImageUrl());
        Assertions.assertNotNull(myUserResponse.getName());
        Assertions.assertNotNull(myUserResponse.getLevel());
        Assertions.assertNotNull(myUserResponse.getBreakthrough());
        Assertions.assertNotNull(myUserResponse.getFriends());
        Assertions.assertNotNull(myUserResponse.getRequests());
        Assertions.assertNotNull(myUserResponse.getUsersRecomended());
    }
}
