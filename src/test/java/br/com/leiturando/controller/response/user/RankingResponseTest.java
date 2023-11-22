package br.com.leiturando.controller.response.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class RankingResponseTest {
    public static RankingResponse builderRankingResponse() {
        return RankingResponse
                .builder()
                .labels(List.of("Gustavo", "Jonas", "Brena", "Henrique"))
                .datasets(List.of(RankingResponse.Datasets
                        .builder()
                        .data(List.of(1, 2, 3, 4))
                        .build()))
                .build();
    }

    @Test
    void buildRankingResponseCorrectly() {
        var rankingResponse = builderRankingResponse();

        Assertions.assertNotNull(rankingResponse.getLabels());
        Assertions.assertNotNull(rankingResponse.getDatasets());
    }
}
