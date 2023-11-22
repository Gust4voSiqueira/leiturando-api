package br.com.leiturando.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FriendshipTest {
    public static Friendship builderFriendship() {
        User user = UserTest.builderUser();
        User friend = UserTest.builderUser();

        return Friendship.builder()
                .id(1L)
                .user(user)
                .friend(friend)
                .build();
    }

    @Test
    void buildFriendshipCorrectly(){
        var result = builderFriendship();

        Assertions.assertNotNull(result.getId());
        Assertions.assertNotNull(result.getUser());
        Assertions.assertNotNull(result.getFriend());
    }
}
