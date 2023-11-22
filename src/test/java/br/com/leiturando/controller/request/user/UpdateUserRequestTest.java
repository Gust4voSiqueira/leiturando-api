package br.com.leiturando.controller.request.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static br.com.leiturando.Consts.PASSWORD_DEFAULT;

public class UpdateUserRequestTest {
    public static UpdateUserRequest builderUpdateUserRequest() {
        return UpdateUserRequest
                .builder()
                .name("Gustavo")
                .dateOfBirth(LocalDateTime.of(2000, Month.DECEMBER, 3, 6,30,40,50000))
                .characterName("Batman")
                .password(PASSWORD_DEFAULT)
                .newPassword(PASSWORD_DEFAULT)
                .confirmNewPassword(PASSWORD_DEFAULT)
                .build();
    }

    @Test
    void buildUpdateUserRequestCorrectly() {
        var updateUserRequest = builderUpdateUserRequest();

        Assertions.assertNotNull(updateUserRequest.getCharacterName());
        Assertions.assertNotNull(updateUserRequest.getName());
        Assertions.assertNotNull(updateUserRequest.getDateOfBirth());
        Assertions.assertNotNull(updateUserRequest.getCharacterName());
        Assertions.assertNotNull(updateUserRequest.getPassword());
        Assertions.assertNotNull(updateUserRequest.getNewPassword());
        Assertions.assertNotNull(updateUserRequest.getConfirmNewPassword());
    }
}
