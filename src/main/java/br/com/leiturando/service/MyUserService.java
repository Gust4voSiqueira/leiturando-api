package br.com.leiturando.service;

import br.com.leiturando.controller.response.ListRequestsResponse;
import br.com.leiturando.controller.response.MyUserResponse;
import br.com.leiturando.controller.response.RecommendedFriendsResponse;
import br.com.leiturando.controller.response.UserResponse;
import br.com.leiturando.entity.User;
import br.com.leiturando.mapper.MyUserMapper;
import br.com.leiturando.mapper.UserMapper;
import br.com.leiturando.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyUserService {
    @Autowired
    MyUserMapper myUserMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RequestsService requestsService;

    @Autowired
    FriendshipService friendshipService;

    public MyUserResponse myUserService(String email) {
        User user = userRepository.findByEmail(email);
        List<ListRequestsResponse> requestsOfMyUser = requestsService.searchRequests(user);
        List<UserResponse> friends = friendshipService.listFriendship(user);
        List<RecommendedFriendsResponse> usersRecommended = searchUsersRecommended(user, requestsOfMyUser, friends);

        return myUserMapper.myUserDtoToResponse(user, friends, requestsOfMyUser, usersRecommended);
    }

    private List<RecommendedFriendsResponse> searchUsersRecommended(User user, List<ListRequestsResponse> requestsOfMyUser, List<UserResponse> friends) {
        PageRequest firstPageWithTwoElements = PageRequest.of(0, 10);
        Page<User> users = userRepository.findAll(firstPageWithTwoElements);

        List<Long> myFriendshipIds = friends.stream().map(UserResponse::getId).collect(Collectors.toList());
        List<Long> requestsIds = requestsOfMyUser.stream().map(ListRequestsResponse::getId).collect(Collectors.toList());

        return users.getContent().stream().filter(userLocal -> user != userLocal)
                .map(userLocal -> userMapper.userToRecommendedFriend(userLocal, requestsService.searchMutualFriends(user, userLocal)))
                .filter(userLocal -> !myFriendshipIds.contains(userLocal.getId()))
                .filter(userLocal -> !requestsIds.contains(userLocal.getId()))
                .collect(Collectors.toList());
    }
}
