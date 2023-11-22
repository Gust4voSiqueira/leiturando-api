package br.com.leiturando.controller.response.requests;

import br.com.leiturando.controller.response.user.UserResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RequestResponse {
    private List<UserResponse> friends;
    private List<ListRequestsResponse> requests;
    private List<ListRequestsResponse> requestsSend;
    private List<RecommendedFriendsResponse> usersRecommended;
}
