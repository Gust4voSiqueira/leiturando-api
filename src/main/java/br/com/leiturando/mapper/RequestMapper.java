package br.com.leiturando.mapper;

import br.com.leiturando.controller.response.*;
import br.com.leiturando.entity.FriendRequests;
import br.com.leiturando.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RequestMapper {

    public FriendRequests createRequest(User requester, User requested) {
        return new FriendRequests(requester, requested);
    }

    public SendRequestResponse requestToResponse(FriendRequests friendRequest) {
        return new SendRequestResponse(friendRequest.getRequester().getId(), friendRequest.getRequested().getId());
    }

    public ListRequestsResponse myUserResponse(User user, String image, Integer mutualFriends) {
        return new ListRequestsResponse(user.getId(), user.getName(), image, mutualFriends);
    }

    public RequestResponse getRequests(List<UserResponse> friends, List<ListRequestsResponse> requests,  List<ListRequestsResponse> requestsSend, List<RecommendedFriendsResponse> usersRecommended) {
        return RequestResponse
                .builder()
                .friends(friends)
                .usersRecommended(usersRecommended)
                .requests(requests)
                .requestsSend(requestsSend)
                .build();
    }
}
