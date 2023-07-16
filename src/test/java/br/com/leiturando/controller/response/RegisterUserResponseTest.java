package br.com.leiturando.controller.response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RegisterUserResponseTest {
    public static RegisterUserResponse builderRegisterUserResponse() {
        return RegisterUserResponse
                .builder()
                .urlImage("Batman")
                .name("Gustavo")
                .email("gustavosiqueira082@gmail.com")
                .build();
    }

    @Test
    void buildRegisterUserResponseCorrectly() {
        var registerUserResponse = builderRegisterUserResponse();

        Assertions.assertNotNull(registerUserResponse.getUrlImage());
        Assertions.assertNotNull(registerUserResponse.getName());
        Assertions.assertNotNull(registerUserResponse.getEmail());
    }
}
