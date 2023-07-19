package br.com.leiturando.controller.response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class MyUserResponseTest {
    public static MyUserResponse builderMyUserResponse() {
        return MyUserResponse
                .builder()
                .imageUrl("Batman")
                .name("Gustavo")
                .level(1)
                .breakthrough(0)
                .build();
    }

    @Test
    void buildMyUserResponseCorrectly() {
        var myUserResponse = builderMyUserResponse();

        Assertions.assertNotNull(myUserResponse.getImageUrl());
        Assertions.assertNotNull(myUserResponse.getName());
        Assertions.assertNotNull(myUserResponse.getLevel());
        Assertions.assertNotNull(myUserResponse.getBreakthrough());
    }
}
