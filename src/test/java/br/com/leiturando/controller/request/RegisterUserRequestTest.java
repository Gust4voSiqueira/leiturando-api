package br.com.leiturando.controller.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static br.com.leiturando.Consts.PASSWORD_DEFAULT;

public class RegisterUserRequestTest {
    public static RegisterUserRequest builderUserRequest() {
        return RegisterUserRequest
                .builder()
                .characterName("Batman")
                .name("Gustavo")
                .email("gustavosiqueira082@gmail.com")
                .password(PASSWORD_DEFAULT)
                .confirmPassword(PASSWORD_DEFAULT)
                .build();
    }

    @Test
    void buildRegisterUserCorrectly() {
        var user = builderUserRequest();

        Assertions.assertNotNull(user.getCharacterName());
        Assertions.assertNotNull(user.getName());
        Assertions.assertNotNull(user.getEmail());
        Assertions.assertNotNull(user.getPassword());
        Assertions.assertNotNull(user.getConfirmPassword());
    }

}
