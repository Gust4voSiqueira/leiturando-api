package br.com.leiturando.controller.response;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
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
    public void buildRegisterUserResponseCorrectly() {
        var registerUserResponse = builderRegisterUserResponse();

        Assert.assertNotNull(registerUserResponse.getUrlImage());
        Assert.assertNotNull(registerUserResponse.getName());
        Assert.assertNotNull(registerUserResponse.getEmail());
    }
}
