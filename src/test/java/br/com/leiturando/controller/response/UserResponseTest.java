package br.com.leiturando.controller.response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserResponseTest {
    public static UserResponse builderUserResponse() {
        return UserResponse
                .builder()
                .id(1L)
                .image("Batman")
                .name("Gustavo")
                .build();
    }

    @Test
    void builderUserResponseCorrectly() {
        var userResponse = builderUserResponse();

        Assertions.assertNotNull(userResponse.getId());
        Assertions.assertNotNull(userResponse.getImage());
        Assertions.assertNotNull(userResponse.getName());
    }
}
