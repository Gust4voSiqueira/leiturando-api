package br.com.leiturando.controller.response;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class RecommendedFriendsResponseTest {
    public static RecommendedFriendsResponse builderRecommendedFriendsResponse() {
        return RecommendedFriendsResponse
                .builder()
                .id(1L)
                .name("Gustavo")
                .urlImage("Batman")
                .mutualFriends(1)
                .build();
    }

    @Test
    public void buildRecommendedFriendsResponseCorrectly() {
        var recommendedFriendsResponse = builderRecommendedFriendsResponse();

        Assert.assertNotNull(recommendedFriendsResponse.getId());
        Assert.assertNotNull(recommendedFriendsResponse.getName());
        Assert.assertNotNull(recommendedFriendsResponse.getUrlImage());
        Assert.assertNotNull(recommendedFriendsResponse.getMutualFriends());
    }
}
