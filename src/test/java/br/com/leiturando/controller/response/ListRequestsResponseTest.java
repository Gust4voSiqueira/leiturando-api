package br.com.leiturando.controller.response;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ListRequestsResponseTest {
    public static ListRequestsResponse builderListRequestsResponse() {
        return ListRequestsResponse
                .builder()
                .id(1L)
                .name("Gustavo")
                .urlImage("Batman")
                .mutualFriends(1)
                .build();
    }

    @Test
    public void buildListRequestsResponseCorrectly() {
        var listRequestsResponse = builderListRequestsResponse();

        Assert.assertNotNull(listRequestsResponse.getId());
        Assert.assertNotNull(listRequestsResponse.getName());
        Assert.assertNotNull(listRequestsResponse.getUrlImage());
        Assert.assertNotNull(listRequestsResponse.getMutualFriends());
    }
}
