package br.com.leiturando.service;

import br.com.leiturando.controller.response.UserResponse;
import br.com.leiturando.entity.Friendship;
import br.com.leiturando.entity.User;
import br.com.leiturando.mapper.UserMapper;
import br.com.leiturando.repository.FriendshipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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

    User user1;
    User user2;
    UserResponse userResponse;
    List<Friendship> friendship;

    @BeforeEach
    public void init() {
        user2 = User.builder()
                .id(2L)
                .name("rogerio")
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
        user1 = User.builder()
                .id(1L)
                .name("gustavo")
                .email("gustavo@gmail.com")
                .password(PASSWORD_DEFAULT)
                .image("Batman")
                .level(1)
                .breakthrough(0)
                .friendships(friendship)
                .build();
        friendship = List.of(Friendship
                .builder()
                        .id(1L)
                        .user(user1)
                        .friend(user2)
                .build());
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
}

