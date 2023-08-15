package br.com.leiturando.controller.response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RecommendedFriendsResponseTest {
    public static RecommendedFriendsResponse builderRecommendedFriendsResponse() {
        return RecommendedFriendsResponse
                .builder()
                .id(1L)
                .name("Gustavo")
                .image("Batman")
                .mutualFriends(1)
                .build();
    }

    @Test
    void buildRecommendedFriendsResponseCorrectly() {
        var recommendedFriendsResponse = builderRecommendedFriendsResponse();

        Assertions.assertNotNull(recommendedFriendsResponse.getId());
        Assertions.assertNotNull(recommendedFriendsResponse.getName());
        Assertions.assertNotNull(recommendedFriendsResponse.getImage());
        Assertions.assertNotNull(recommendedFriendsResponse.getMutualFriends());
    }
}
