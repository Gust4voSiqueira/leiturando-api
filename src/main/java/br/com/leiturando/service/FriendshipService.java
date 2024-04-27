package br.com.leiturando.service;

import br.com.leiturando.controller.response.user.SearchUserResponse;
import br.com.leiturando.controller.response.user.UserResponse;
import br.com.leiturando.domain.TypesCard;
import br.com.leiturando.entity.FriendRequests;
import br.com.leiturando.entity.Friendship;
import br.com.leiturando.entity.User;
import br.com.leiturando.mapper.UserMapper;
import br.com.leiturando.repository.FriendRequestRepository;
import br.com.leiturando.repository.FriendshipRepository;
import br.com.leiturando.repository.UserRepository;
import com.amazonaws.services.pinpoint.model.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FriendshipService {
    @Autowired
    FriendshipRepository friendshipRepository;

    @Autowired
    FriendRequestRepository requestRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    public List<UserResponse> listFriendship(User user) {
        List<Friendship> friendshipMyUser = friendshipRepository.findAllByUserOrFriend(user);

        return friendshipMyUser
                .stream()
                .map(friendship -> {
                    if(friendship.getUser() != user) {
                        return friendship.getUser();
                    }

                    return friendship.getFriend();
                })
                .filter(friendshipUser -> friendshipUser != user)
                .map(friendship -> userMapper.userToResponse(friendship))
                .collect(Collectors.toList());
    }

    public List<SearchUserResponse> searchUsers(String filter, String email) {
        User myUser = userRepository.findByEmail(email);

        return userRepository.filterUsers(capitalizeFirstLetter(filter))
                .stream()
                .filter(user -> !user.equals(myUser))
                .map(user -> {
                    Integer mutualFriends = searchMutualFriends(myUser, user);

                    return userMapper.userToSearchUser(user, mutualFriends, typeFriend(myUser, user));
                })
                .collect(Collectors.toList());
    }

    public ResponseEntity<String> unFriend(String email, Long idFriend) {
        User myUser = userRepository.findByEmail(email);
        Optional<User> friend = userRepository.findById(idFriend);

        if(friend.isEmpty()) {
            throw new BadRequestException("Não existe um usuário com o Id informado");
        }

        Optional<Friendship> friendship = friendshipRepository.findFriendshipById(friend.get(), myUser);

        if(friendship.isEmpty()) {
            throw new BadRequestException("Parece que vocês não são amigos");
        }

        friendshipRepository.deleteById(friendship.get().getId());

        return ResponseEntity.ok().body("Amizade desfeita.");
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    private TypesCard typeFriend(User myUser, User userSearch) {
        Optional<Friendship> isFriend = friendshipRepository.findAllByUserOrFriend(myUser)
                .stream()
                .filter(friend -> friend.getFriend().equals(userSearch) || friend.getUser().equals(userSearch))
                .findFirst();

        if(isFriend.isPresent()) {
            return TypesCard.FRIEND;
        }

        Optional<List<FriendRequests>> myRequestsReceived = requestRepository.findAllByRequestedId(myUser.getId());

        if(myRequestsReceived.isPresent()) {
            Optional<FriendRequests> isRequestReceived = myRequestsReceived.get()
                    .stream()
                    .filter(request -> request.getRequester().equals(userSearch))
                    .findFirst();

            if(isRequestReceived.isPresent()) {
                return TypesCard.REQUEST_RECEIVED;
            }
        }

        Optional<List<FriendRequests>> myRequestsSend = requestRepository.findAllByRequesterId(myUser.getId());

        if(myRequestsSend.isPresent()) {
            Optional<FriendRequests> isRequestSend = myRequestsSend.get()
                    .stream()
                    .filter(request -> request.getRequested().equals(userSearch))
                    .findFirst();

            if(isRequestSend.isPresent()) {
                return TypesCard.REQUEST_SEND;
            }
        }

        return TypesCard.NO_REQUEST;
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
}
