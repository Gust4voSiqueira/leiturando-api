package br.com.leiturando.service;

import br.com.leiturando.controller.response.UserResponse;
import br.com.leiturando.entity.Friendship;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import br.com.leiturando.mapper.UserMapper;
import br.com.leiturando.repository.FriendshipRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static br.com.leiturando.Consts.PASSWORD_DEFAULT;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class FriendshipServiceTest {
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

    @Before
    public void init() {
        user1 = UserTest.builderUser();
        user2 = User.builder()
                .id(2L)
                .name("rogerio")
                .email("rogerio@gmail.com")
                .password(PASSWORD_DEFAULT)
                .imageUrl("Batman")
                .friendships(List.of())
                .build();
        userResponse = UserResponse
                .builder()
                .urlImage(user2.getImageUrl())
                .name(user2.getName())
                .build();
        friendship = List.of(new Friendship(1L, user1, user2));
    }

    @Test
    public void listFriendshipUserSuccess() {
        user1.setFriendships(friendship);

        when(friendshipRepository.findAllByUserOrFriend(user1)).thenReturn(friendship);
        when(userMapper.userToResponse(user2)).thenReturn(userResponse);

        var result = friendshipService.listFriendship(user1);

        assertEquals(List.of(userResponse), result);
    }

    @Test
    public void listFriendshipUserFriendSuccess() {
        friendship = List.of(new Friendship(1L, user2, user1));
        user1.setFriendships(friendship);

        when(friendshipRepository.findAllByUserOrFriend(user1)).thenReturn(friendship);
        when(userMapper.userToResponse(user2)).thenReturn(userResponse);

        var result = friendshipService.listFriendship(user1);

        assertEquals(List.of(userResponse), result);
    }

    @Test
    public void listFriendshipUserNotFriend() {
        friendship = List.of();

        when(friendshipRepository.findAllByUserOrFriend(user1)).thenReturn(friendship);
        when(userMapper.userToResponse(user2)).thenReturn(userResponse);

        var result = friendshipService.listFriendship(user1);

        assertEquals(0, result.size());
    }
}

