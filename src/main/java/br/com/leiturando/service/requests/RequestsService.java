package br.com.leiturando.service.requests;

import br.com.leiturando.controller.response.ListRequestsResponse;
import br.com.leiturando.controller.response.RecommendedFriendsResponse;
import br.com.leiturando.controller.response.RequestResponse;
import br.com.leiturando.controller.response.UserResponse;
import br.com.leiturando.entity.FriendRequests;
import br.com.leiturando.entity.User;
import br.com.leiturando.mapper.RequestMapper;
import br.com.leiturando.mapper.UserMapper;
import br.com.leiturando.repository.FriendRequestRepository;
import br.com.leiturando.repository.UserRepository;
import br.com.leiturando.service.FileService;
import br.com.leiturando.service.FriendshipService;
import com.amazonaws.services.pinpoint.model.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Autowired
    FileService fileService;

    public RequestResponse getRequests(String email) {
        User user = userRepository.findByEmail(email);

        List<ListRequestsResponse> requestsReceived = searchRequestsReceived(user);
        List<ListRequestsResponse> requestsSend = searchRequestsSend(user);
        List<UserResponse> friends = friendshipService.listFriendship(user);
        List<RecommendedFriendsResponse> usersRecommended = searchUsersRecommended(user, requestsReceived, friends, requestsSend);

        return requestMapper.getRequests(friends, requestsReceived, requestsSend, usersRecommended);
    }

    public List<ListRequestsResponse> searchRequestsReceived(User user) {
        List<FriendRequests> requests = friendRequestRepository.findAllByRequestedId(user.getId());

        if(requests.isEmpty()) {
            return List.of();
        }

        List<User> requestsReceivedUsers = requests.stream().map(request -> userRepository.findById(request.getRequester().getId()).get()).collect(Collectors.toList());

        return requestsReceivedUsers.stream().map(userLocal -> {
            try {
                String image = fileService.downloadFile(userLocal.getImage());

                return requestMapper.myUserResponse(userLocal, image, searchMutualFriends(user, userLocal));
            } catch (IOException e) {
                throw new BadRequestException(e.getMessage());
            }
        }).collect(Collectors.toList());
    }

    public List<ListRequestsResponse> searchRequestsSend(User user) {
        List<FriendRequests> requests = friendRequestRepository.findAllByRequesterId(user.getId());

        if(requests.isEmpty()) {
            return List.of();
        }

        List<User> requestsSendUsers = requests
                .stream()
                .map(request -> userRepository.findById(request.getRequested().getId()).get())
                .collect(Collectors.toList());

        return requestsSendUsers
                .stream()
                .map(userLocal -> {
                    try {
                        String image = fileService.downloadFile(userLocal.getImage());

                        return requestMapper.myUserResponse(userLocal, image, searchMutualFriends(user, userLocal));
                    } catch (IOException e) {
                        throw new BadRequestException(e.getMessage());
                    }
                })
                .collect(Collectors.toList());
    }

    public Integer searchMutualFriends(User myUser, User userCompared) {
        List<User> myListFriends = myUser.getFriendships().stream()
                .flatMap(friendship -> Stream.of(friendship.getUser(), friendship.getFriend()))
                .filter(user -> user != myUser)
                .distinct()
                .collect(Collectors.toList());

        List<User> listFriendsMyFriendship = userCompared.getFriendships().stream()
                .flatMap(friendship -> Stream.of(friendship.getUser(), friendship.getFriend()))
                .filter(user -> user != myUser)
                .distinct()
                .collect(Collectors.toList());

        return (int) myListFriends.stream().filter(listFriendsMyFriendship::contains).count();
    }

    public List<RecommendedFriendsResponse> searchUsersRecommended(User user, List<ListRequestsResponse> requestsOfMyUser, List<UserResponse> friends, List<ListRequestsResponse> requestsSend) {
        PageRequest firstPageWithTwoElements = PageRequest.of(0, 10);
        Page<User> users = userRepository.findAll(firstPageWithTwoElements);

        List<Long> myFriendshipIds = friends.stream().map(UserResponse::getId).collect(Collectors.toList());
        List<Long> requestsIds = requestsOfMyUser.stream().map(ListRequestsResponse::getId).collect(Collectors.toList());
        List<Long> requestsSendIds = requestsSend.stream().map(ListRequestsResponse::getId).collect(Collectors.toList());

        return users.getContent().stream().filter(userLocal -> user != userLocal)
                .filter(userLocal -> !myFriendshipIds.contains(userLocal.getId()))
                .filter(userLocal -> !requestsIds.contains(userLocal.getId()))
                .filter(userLocal -> !requestsSendIds.contains(userLocal.getId()))
                .map(userLocal -> {
                    try {
                        String image = fileService.downloadFile(userLocal.getImage());

                        return userMapper.userToRecommendedFriend(userLocal, image, searchMutualFriends(user, userLocal));
                    } catch (IOException e) {
                        throw new BadRequestException(e.getMessage());
                    }

                    })
                .collect(Collectors.toList());
    }
}
