package br.com.leiturando.mapper;

import br.com.leiturando.controller.response.requests.RecommendedFriendsResponse;
import br.com.leiturando.controller.response.user.SearchUserResponse;
import br.com.leiturando.controller.response.user.UserResponse;
import br.com.leiturando.domain.TypesCard;
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
                    .image(user.getImage())
                    .name(user.getName())
                .build();

        Assertions.assertEquals(expected.getId(), result.getId());
        Assertions.assertEquals(expected.getImage(), result.getImage());
        Assertions.assertEquals(expected.getName(), result.getName());
    }

    @Test
    void setUserToRecommendedFriend() {
        RecommendedFriendsResponse result = userMapper.userToRecommendedFriend(user, 1);
        RecommendedFriendsResponse expected = RecommendedFriendsResponse
                .builder()
                    .id(user.getId())
                    .name(user.getName())
                    .image(user.getImage())
                    .mutualFriends(1)
                .build();

        Assertions.assertEquals(expected.getId(), result.getId());
        Assertions.assertEquals(expected.getImage(), result.getImage());
        Assertions.assertEquals(expected.getName(), result.getName());
        Assertions.assertEquals(expected.getMutualFriends(), result.getMutualFriends());
    }

    @Test
    void setUserToSearchUser() {
        SearchUserResponse result = userMapper.userToSearchUser(user, 1, TypesCard.FRIEND);
        SearchUserResponse expected = SearchUserResponse
                .builder()
                .id(user.getId())
                .name(user.getName())
                .image(user.getImage())
                .mutualFriends(1)
                .typeCard(TypesCard.FRIEND)
                .build();

        Assertions.assertEquals(expected.getId(), result.getId());
        Assertions.assertEquals(expected.getName(), result.getName());
        Assertions.assertEquals(expected.getImage(), result.getImage());
        Assertions.assertEquals(expected.getMutualFriends(), result.getMutualFriends());
        Assertions.assertEquals(expected.getTypeCard(), result.getTypeCard());
    }
}
