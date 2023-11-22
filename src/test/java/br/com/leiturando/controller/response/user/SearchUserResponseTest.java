package br.com.leiturando.controller.response.user;

import br.com.leiturando.domain.TypesCard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SearchUserResponseTest {
    public static SearchUserResponse builderSearchUserResponse() {
        return SearchUserResponse
                .builder()
                .id(1L)
                .image("Batman")
                .name("Gustavo")
                .mutualFriends(1)
                .typeCard(TypesCard.FRIEND)
                .build();
    }

    @Test
    void builderSearchUserResponseCorrectly() {
        var searchUserResponse = builderSearchUserResponse();

        Assertions.assertNotNull(searchUserResponse.getId());
        Assertions.assertNotNull(searchUserResponse.getImage());
        Assertions.assertNotNull(searchUserResponse.getName());
        Assertions.assertNotNull(searchUserResponse.getMutualFriends());
        Assertions.assertNotNull(searchUserResponse.getTypeCard());
    }
}
