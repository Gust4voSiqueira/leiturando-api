package br.com.leiturando.service;

import br.com.leiturando.controller.response.user.SearchUserResponse;
import br.com.leiturando.controller.response.user.UserResponse;
import br.com.leiturando.domain.TypesCard;
import br.com.leiturando.entity.FriendRequests;
import br.com.leiturando.entity.Friendship;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import br.com.leiturando.mapper.UserMapper;
import br.com.leiturando.repository.FriendRequestRepository;
import br.com.leiturando.repository.FriendshipRepository;
import br.com.leiturando.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static br.com.leiturando.Consts.PASSWORD_DEFAULT;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FriendshipServiceTest {
    @InjectMocks
    FriendshipService friendshipService;
    @Mock
    UserMapper userMapper;
    @Mock
    FriendshipRepository friendshipRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    FriendRequestRepository requestRepository;

    User user1;
    User user2;
    UserResponse userResponse;
    List<Friendship> friendship;
    SearchUserResponse searchUserResponse;
    FriendRequests friendRequestsReceived;
    FriendRequests friendRequestsSend;

    @BeforeEach
    public void init() {
        user1 = UserTest.builderUser();
        user2 = User.builder()
                .id(2L)
                .name("Rogerio")
                .email("rogerio@gmail.com")
                .password(PASSWORD_DEFAULT)
                .image("Batman")
                .friendships(List.of())
                .build();
        userResponse = UserResponse
                .builder()
                .image(user2.getImage())
                .name(user2.getName())
                .build();
        friendship = List.of(Friendship
                .builder()
                        .id(1L)
                        .user(user1)
                        .friend(user2)
                .build());
        searchUserResponse = SearchUserResponse
                .builder()
                .id(user2.getId())
                .image(user2.getImage())
                .name(user2.getName())
                .mutualFriends(1)
                .typeCard(TypesCard.FRIEND)
                .build();
        friendRequestsReceived = FriendRequests
                .builder()
                .id(1L)
                .requester(user2)
                .requested(user1)
                .build();
        friendRequestsSend =  FriendRequests
                .builder()
                .id(1L)
                .requester(user1)
                .requested(user2)
                .build();
    }

    @Test
    void listFriendshipUserSuccess() {
        when(friendshipRepository.findAllByUserOrFriend(user1)).thenReturn(friendship);
        when(userMapper.userToResponse(user2)).thenReturn(userResponse);

        var result = friendshipService.listFriendship(user1);

        Assertions.assertEquals(List.of(userResponse), result);
    }

    @Test
    void listFriendshipUserFriendSuccess() {
        friendship = List.of(new Friendship(1L, user2, user1));

        when(friendshipRepository.findAllByUserOrFriend(user1)).thenReturn(friendship);
        when(userMapper.userToResponse(user2)).thenReturn(userResponse);

        var result = friendshipService.listFriendship(user1);

        Assertions.assertEquals(List.of(userResponse), result);
    }

    @Test
    void listFriendshipUserNotFriend() {
        friendship = List.of();

        when(friendshipRepository.findAllByUserOrFriend(user1)).thenReturn(friendship);

        var result = friendshipService.listFriendship(user1);

        Assertions.assertEquals(0, result.size());
    }

    @Test
    void searchUserFriendCorrectly() {
        when(userRepository.findByEmail(user1.getEmail())).thenReturn(user1);
        when(userRepository.filterUsers(user2.getName())).thenReturn(List.of(user2));
        when(friendshipRepository.findAllByUserOrFriend(user1)).thenReturn(friendship);
        when(userMapper.userToSearchUser(user2, 0, TypesCard.FRIEND)).thenReturn(searchUserResponse);

        var result = friendshipService.searchUsers(user2.getName(), user1.getEmail());

        Assertions.assertEquals(List.of(searchUserResponse), result);
    }

    @Test
    void searchUserRequestReceivedCorrectly() {
        when(userRepository.findByEmail(user1.getEmail())).thenReturn(user1);
        when(userRepository.filterUsers(user2.getName())).thenReturn(List.of(user2));
        when(friendshipRepository.findAllByUserOrFriend(user1)).thenReturn(List.of());
        when(requestRepository.findAllByRequestedId(user1.getId())).thenReturn(Optional.of(List.of(friendRequestsReceived)));
        when(userMapper.userToSearchUser(user2, 0, TypesCard.REQUEST_RECEIVED)).thenReturn(searchUserResponse);

        var result = friendshipService.searchUsers(user2.getName(), user1.getEmail());

        Assertions.assertEquals(List.of(searchUserResponse), result);
    }

    @Test
    void searchUserRequestSendCorrectly() {
        when(userRepository.findByEmail(user1.getEmail())).thenReturn(user1);
        when(userRepository.filterUsers(user2.getName())).thenReturn(List.of(user2));
        when(friendshipRepository.findAllByUserOrFriend(user1)).thenReturn(List.of());
        when(requestRepository.findAllByRequestedId(user1.getId())).thenReturn(Optional.empty());
        when(requestRepository.findAllByRequesterId(user1.getId())).thenReturn(Optional.of(List.of(friendRequestsSend)));
        when(userMapper.userToSearchUser(user2, 0, TypesCard.REQUEST_SEND)).thenReturn(searchUserResponse);

        var result = friendshipService.searchUsers(user2.getName(), user1.getEmail());

        Assertions.assertEquals(List.of(searchUserResponse), result);
    }

    @Test
    void searchUserNotRequestCorrectly() {
        when(userRepository.findByEmail(user1.getEmail())).thenReturn(user1);
        when(userRepository.filterUsers(user2.getName())).thenReturn(List.of(user2));
        when(friendshipRepository.findAllByUserOrFriend(user1)).thenReturn(List.of());
        when(requestRepository.findAllByRequestedId(user1.getId())).thenReturn(Optional.empty());
        when(requestRepository.findAllByRequesterId(user1.getId())).thenReturn(Optional.empty());
        when(userMapper.userToSearchUser(user2, 0, TypesCard.NO_REQUEST)).thenReturn(searchUserResponse);

        var result = friendshipService.searchUsers(user2.getName(), user1.getEmail());

        Assertions.assertEquals(List.of(searchUserResponse), result);
    }
}

