package br.com.leiturando.mapper;


import br.com.leiturando.controller.response.RecommendedFriendsResponse;
import br.com.leiturando.controller.response.UserResponse;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {
    @InjectMocks
    UserMapper userMapper;

    User user;

    @BeforeEach
    public void init() {
        user = UserTest.builderUser();
    }

    @Test
    void setUserToResponse() {
        UserResponse result = userMapper.userToResponse(user);
        UserResponse expected = UserResponse
                .builder()
                    .id(user.getId())
                    .urlImage(user.getImageUrl())
                    .name(user.getName())
                .build();

        Assertions.assertEquals(expected.getId(), result.getId());
        Assertions.assertEquals(expected.getUrlImage(), result.getUrlImage());
        Assertions.assertEquals(expected.getName(), result.getName());
    }

    @Test
    void setUserToRecommendedFriend() {
        RecommendedFriendsResponse result = userMapper.userToRecommendedFriend(user, 1);
        RecommendedFriendsResponse expected = RecommendedFriendsResponse
                .builder()
                    .id(user.getId())
                    .name(user.getName())
                    .urlImage(user.getImageUrl())
                    .mutualFriends(1)
                .build();

        Assertions.assertEquals(expected.getId(), result.getId());
        Assertions.assertEquals(expected.getUrlImage(), result.getUrlImage());
        Assertions.assertEquals(expected.getName(), result.getName());
        Assertions.assertEquals(expected.getMutualFriends(), result.getMutualFriends());
    }
}
