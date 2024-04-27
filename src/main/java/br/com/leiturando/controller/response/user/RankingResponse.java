package br.com.leiturando.controller.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class RankingResponse {
    private List<String> labels;
    private List<Datasets> datasets;

    @Builder
    @Getter
    public static class Datasets {
        private List<Integer> data;
    }
}
