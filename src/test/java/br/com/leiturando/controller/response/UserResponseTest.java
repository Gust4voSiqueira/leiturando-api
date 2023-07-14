package br.com.leiturando.controller.response;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class UserResponseTest {
    public static UserResponse builderUserResponse() {
        return UserResponse
                .builder()
                .id(1L)
                .urlImage("Batman")
                .name("Gustavo")
                .build();
    }

    @Test
    public void builderUserResponseCorrectly() {
        var userResponse = builderUserResponse();

        Assert.assertNotNull(userResponse.getId());
        Assert.assertNotNull(userResponse.getUrlImage());
        Assert.assertNotNull(userResponse.getName());
    }
}
