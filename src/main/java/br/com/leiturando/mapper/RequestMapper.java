package br.com.leiturando.mapper;

import br.com.leiturando.controller.response.requests.ListRequestsResponse;
import br.com.leiturando.controller.response.requests.RecommendedFriendsResponse;
import br.com.leiturando.controller.response.requests.RequestResponse;
import br.com.leiturando.controller.response.user.UserResponse;
import br.com.leiturando.entity.FriendRequests;
import br.com.leiturando.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RequestMapper {

    public FriendRequests createRequest(User requester, User requested) {
        return new FriendRequests(requester, requested);
    }

    public ListRequestsResponse myUserResponse(User user, Integer mutualFriends) {
        return new ListRequestsResponse(user.getId(), user.getName(), user.getImage(), mutualFriends);
    }

    public RequestResponse getRequests(List<UserResponse> friends, List<ListRequestsResponse> requests, List<ListRequestsResponse> requestsSend, List<RecommendedFriendsResponse> usersRecommended) {
        return RequestResponse
                .builder()
                .friends(friends)
                .usersRecommended(usersRecommended)
                .requests(requests)
                .requestsSend(requestsSend)
                .build();
    }
}
