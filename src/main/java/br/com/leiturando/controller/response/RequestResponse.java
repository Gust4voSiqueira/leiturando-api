package br.com.leiturando.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class RequestResponse {
    private List<UserResponse> friends;
    private List<ListRequestsResponse> requests;
    private List<ListRequestsResponse> requestsSend;
    private List<RecommendedFriendsResponse> usersRecommended;
}
