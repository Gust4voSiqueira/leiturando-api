package br.com.leiturando.mapper;

import br.com.leiturando.controller.response.RecommendedFriendsResponse;
import br.com.leiturando.controller.response.UserResponse;
import br.com.leiturando.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse userToResponse(User user) {
        return new UserResponse(user.getId(), user.getImageUrl(), user.getName());
    }

    public RecommendedFriendsResponse userToRecommendedFriend(User user, String image, Integer mutualFriends) {
        return new RecommendedFriendsResponse(user.getId(), user.getName(), image, mutualFriends);
    }
}
