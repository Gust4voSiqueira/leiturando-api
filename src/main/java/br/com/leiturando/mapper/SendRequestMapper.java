package br.com.leiturando.mapper;

import br.com.leiturando.controller.response.ListRequestsResponse;
import br.com.leiturando.controller.response.SendRequestResponse;
import br.com.leiturando.entity.FriendRequests;
import br.com.leiturando.entity.User;
import org.springframework.stereotype.Component;

@Component
public class SendRequestMapper {

    public FriendRequests createRequest(User requester, User requested) {
        return new FriendRequests(requester, requested);
    }

    public SendRequestResponse requestToResponse(FriendRequests friendRequest) {
        return new SendRequestResponse(friendRequest.getRequester().getId(), friendRequest.getRequested().getId());
    }

    public ListRequestsResponse myUserResponse(User user, Integer mutualFriends) {
        return new ListRequestsResponse(user.getId(), user.getName(), user.getImageUrl(), mutualFriends);
    }
}
