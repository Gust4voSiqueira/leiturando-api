package br.com.leiturando.controller.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Data
@AllArgsConstructor
public class RankingResponse {
    private List<String> labels;
    private List<Datasets> datasets;

    @Data
    @Builder
    public static class Datasets {
        private List<Integer> data;
    }
}
