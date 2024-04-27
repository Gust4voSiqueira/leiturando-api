package br.com.leiturando.service.requests;

import br.com.leiturando.controller.response.requests.ListRequestsResponse;
import br.com.leiturando.controller.response.requests.RecommendedFriendsResponse;
import br.com.leiturando.controller.response.requests.RequestResponse;
import br.com.leiturando.controller.response.user.UserResponse;
import br.com.leiturando.entity.FriendRequests;
import br.com.leiturando.entity.User;
import br.com.leiturando.mapper.RequestMapper;
import br.com.leiturando.mapper.UserMapper;
import br.com.leiturando.repository.FriendRequestRepository;
import br.com.leiturando.repository.UserRepository;
import br.com.leiturando.service.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RequestsService {

    @Autowired
    FriendRequestRepository friendRequestRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FriendshipService friendshipService;

    @Autowired
    RequestMapper requestMapper;

    public RequestResponse getRequests(String email) {
        User user = userRepository.findByEmail(email);

        List<ListRequestsResponse> requestsReceived = searchRequestsReceived(user);
        List<ListRequestsResponse> requestsSend = searchRequestsSend(user);
        List<UserResponse> friends = friendshipService.listFriendship(user);
        List<RecommendedFriendsResponse> usersRecommended = searchUsersRecommended(user, requestsReceived, friends, requestsSend);

        return requestMapper.getRequests(friends, requestsReceived, requestsSend, usersRecommended);
    }

    public List<ListRequestsResponse> searchRequestsReceived(User user) {
        Optional<List<FriendRequests>> requests = friendRequestRepository.findAllByRequestedId(user.getId());

        if(requests.isEmpty()) {
            return List.of();
        }

        List<User> requestsReceivedUsers = requests.get().stream().map(request -> userRepository.findById(request.getRequester().getId()).get()).collect(Collectors.toList());

        return requestsReceivedUsers.stream().map(userLocal -> requestMapper.myUserResponse(userLocal, friendshipService.searchMutualFriends(user, userLocal)))
                .collect(Collectors.toList());
    }

    public List<ListRequestsResponse> searchRequestsSend(User user) {
        Optional<List<FriendRequests>> requests = friendRequestRepository.findAllByRequesterId(user.getId());

        if(requests.isEmpty()) {
            return List.of();
        }

        List<User> requestsSendUsers = requests
                .get()
                .stream()
                .map(request -> userRepository.findById(request.getRequested().getId()).get())
                .collect(Collectors.toList());

        return requestsSendUsers
                .stream()
                .map(userLocal -> requestMapper.myUserResponse(userLocal, friendshipService.searchMutualFriends(user, userLocal)))
                .collect(Collectors.toList());
    }

    public List<RecommendedFriendsResponse> searchUsersRecommended(User user, List<ListRequestsResponse> requestsOfMyUser, List<UserResponse> friends, List<ListRequestsResponse> requestsSend) {
        PageRequest firstPageWithTwoElements = PageRequest.of(0, 10);
        Page<User> users = userRepository.findAll(firstPageWithTwoElements);

        if(users.isEmpty()) {
            return List.of();
        }

        List<Long> myFriendshipIds = friends.stream().map(UserResponse::getId).collect(Collectors.toList());
        List<Long> requestsIds = requestsOfMyUser.stream().map(ListRequestsResponse::getId).collect(Collectors.toList());
        List<Long> requestsSendIds = requestsSend.stream().map(ListRequestsResponse::getId).collect(Collectors.toList());

        return users.getContent().stream().filter(userLocal -> user != userLocal)
                .filter(userLocal -> !myFriendshipIds.contains(userLocal.getId()))
                .filter(userLocal -> !requestsIds.contains(userLocal.getId()))
                .filter(userLocal -> !requestsSendIds.contains(userLocal.getId()))
                .map(userLocal -> userMapper.userToRecommendedFriend(userLocal, friendshipService.searchMutualFriends(user, userLocal)))
                .collect(Collectors.toList());
    }
}
