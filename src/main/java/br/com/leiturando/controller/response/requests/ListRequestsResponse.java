package br.com.leiturando.controller.response.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ListRequestsResponse {
    private Long id;
    private String name;
    private String image;
    private Integer mutualFriends;
}
