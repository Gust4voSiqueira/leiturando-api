package br.com.leiturando.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
@AllArgsConstructor
public class RecommendedFriendsResponse {
    private Long id;
    private String name;
    private String image;
    private Integer mutualFriends;
}
