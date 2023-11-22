package br.com.leiturando.controller.response;

import br.com.leiturando.controller.response.requests.ListRequestsResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ListRequestsResponseTest {
    public static ListRequestsResponse builderListRequestsResponse() {
        return ListRequestsResponse
                .builder()
                .id(1L)
                .name("Gustavo")
                .image("Batman")
                .mutualFriends(1)
                .build();
    }

    @Test
    void buildListRequestsResponseCorrectly() {
        var listRequestsResponse = builderListRequestsResponse();

        Assertions.assertNotNull(listRequestsResponse.getId());
        Assertions.assertNotNull(listRequestsResponse.getName());
        Assertions.assertNotNull(listRequestsResponse.getImage());
        Assertions.assertNotNull(listRequestsResponse.getMutualFriends());
    }
}
