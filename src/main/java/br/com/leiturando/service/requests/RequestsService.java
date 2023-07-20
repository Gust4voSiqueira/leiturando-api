package br.com.leiturando.service.requests;

import br.com.leiturando.controller.response.ListRequestsResponse;
import br.com.leiturando.controller.response.RecommendedFriendsResponse;
import br.com.leiturando.controller.response.RequestResponse;
import br.com.leiturando.controller.response.UserResponse;
import br.com.leiturando.entity.User;
import br.com.leiturando.mapper.RequestMapper;
import br.com.leiturando.mapper.UserMapper;
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

import static br.com.leiturando.domain.Const.CHARACTERS_LIST;

@Service
public class RequestsService {

    @Autowired
    SendRequestsService sendRequestsService;

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

        List<ListRequestsResponse> requestsReceived = sendRequestsService.searchRequestsReceived(user);
        List<ListRequestsResponse> requestsSend = sendRequestsService.searchRequestsSend(user);
        List<UserResponse> friends = friendshipService.listFriendship(user);
        List<RecommendedFriendsResponse> usersRecommended = searchUsersRecommended(user, requestsReceived, friends);

        return requestMapper.getRequests(friends, requestsReceived, requestsSend, usersRecommended);
    }

    public List<RecommendedFriendsResponse> searchUsersRecommended(User user, List<ListRequestsResponse> requestsOfMyUser, List<UserResponse> friends) {
        PageRequest firstPageWithTwoElements = PageRequest.of(0, 10);
        Page<User> users = userRepository.findAll(firstPageWithTwoElements);

        List<Long> myFriendshipIds = friends.stream().map(UserResponse::getId).collect(Collectors.toList());
        List<Long> requestsIds = requestsOfMyUser.stream().map(ListRequestsResponse::getId).collect(Collectors.toList());

        return users.getContent().stream().filter(userLocal -> user != userLocal)
                .filter(userLocal -> !myFriendshipIds.contains(userLocal.getId()))
                .filter(userLocal -> !requestsIds.contains(userLocal.getId()))
                .map(userLocal -> {
                    try {
                        String image = fileService.downloadFile(userLocal.getImageUrl());

                        return userMapper.userToRecommendedFriend(userLocal, image, sendRequestsService.searchMutualFriends(user, userLocal));
                    } catch (IOException e) {
                        throw new BadRequestException(e.getMessage());
                    }

                    })
                .collect(Collectors.toList());
    }
}
