package br.com.leiturando.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class MyUserResponse {
    private String imageUrl;
    private String name;
    private Integer level;
    private Integer breakthrough;
    private List<UserResponse> friends;
    private List<ListRequestsResponse> requests;
    private List<RecommendedFriendsResponse> usersRecomended;
}
