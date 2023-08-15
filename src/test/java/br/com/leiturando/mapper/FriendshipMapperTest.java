package br.com.leiturando.mapper;

import br.com.leiturando.entity.FriendRequests;
import br.com.leiturando.entity.Friendship;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static br.com.leiturando.Consts.PASSWORD_DEFAULT;

@ExtendWith(MockitoExtension.class)
class FriendshipMapperTest {
    @InjectMocks
    FriendshipMapper friendshipMapper;

    FriendRequests friendRequests;
    User user;
    User friend;

    @BeforeEach
    public void init() {
        user = UserTest.builderUser();
        friend = User
                .builder()
                .id(2L)
                .name("rogerio")
                .email("rogerio@gmail.com")
                .password(PASSWORD_DEFAULT)
                .image("Batman")
                .friendships(List.of())
                .build();
        friendRequests = FriendRequests
                .builder()
                .requester(friend)
                .requested(user)
                .build();
    }

    @Test
    void setRequestToFriendship() {
        Friendship result = friendshipMapper.requestToFriendship(friendRequests);
        Friendship expected = Friendship
                .builder()
                .user(user)
                .friend(friend)
                .build();


        Assertions.assertEquals(expected.getUser().getName(), result.getUser().getName());
        Assertions.assertEquals(expected.getFriend().getName(), result.getFriend().getName());
    }
}
