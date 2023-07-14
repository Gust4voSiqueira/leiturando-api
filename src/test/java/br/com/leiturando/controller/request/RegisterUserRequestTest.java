package br.com.leiturando.controller.request;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static br.com.leiturando.Consts.PASSWORD_DEFAULT;

@RunWith(SpringRunner.class)
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
    public void buildRegisterUserCorrectly() {
        var user = builderUserRequest();

        Assert.assertNotNull(user.getCharacterName());
        Assert.assertNotNull(user.getName());
        Assert.assertNotNull(user.getEmail());
        Assert.assertNotNull(user.getPassword());
        Assert.assertNotNull(user.getConfirmPassword());
    }

}
