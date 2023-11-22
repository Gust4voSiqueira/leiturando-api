package br.com.leiturando.mapper;

import br.com.leiturando.controller.response.requests.RecommendedFriendsResponse;
import br.com.leiturando.controller.response.user.SearchUserResponse;
import br.com.leiturando.controller.response.user.UserResponse;
import br.com.leiturando.domain.TypesCard;
import br.com.leiturando.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse userToResponse(User user) {
        return new UserResponse(user.getId(), user.getImage(), user.getName());
    }

    public RecommendedFriendsResponse userToRecommendedFriend(User user, Integer mutualFriends) {
        return new RecommendedFriendsResponse(user.getId(), user.getName(), user.getImage(), mutualFriends);
    }

    public SearchUserResponse userToSearchUser(User user, Integer mutualFriend, TypesCard typeCard) {
        return SearchUserResponse
                .builder()
                .id(user.getId())
                .image(user.getImage())
                .name(user.getName())
                .mutualFriends(mutualFriend)
                .typeCard(typeCard)
                .build();
    }
}
