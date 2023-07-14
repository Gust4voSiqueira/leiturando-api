package br.com.leiturando.controller.response;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
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
    public void buildMyUserResponseCorrectly() {
        var myUserResponse = builderMyUserResponse();

        Assert.assertNotNull(myUserResponse.getImageUrl());
        Assert.assertNotNull(myUserResponse.getName());
        Assert.assertNotNull(myUserResponse.getLevel());
        Assert.assertNotNull(myUserResponse.getBreakthrough());
        Assert.assertNotNull(myUserResponse.getFriends());
        Assert.assertNotNull(myUserResponse.getRequests());
        Assert.assertNotNull(myUserResponse.getUsersRecomended());
    }
}
