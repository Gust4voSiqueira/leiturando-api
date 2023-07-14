package br.com.leiturando.mapper;

import br.com.leiturando.controller.response.ListRequestsResponse;
import br.com.leiturando.controller.response.MyUserResponse;
import br.com.leiturando.controller.response.RecommendedFriendsResponse;
import br.com.leiturando.controller.response.UserResponse;
import br.com.leiturando.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyUserMapper {
    public MyUserResponse myUserDtoToResponse(User user, List<UserResponse> friends, List<ListRequestsResponse> requests, List<RecommendedFriendsResponse> usersRecommended) {
        return new MyUserResponse(
                user.getImageUrl(),
                user.getName(),
                user.getLevel(),
                user.getBreakthrough(),
                friends,
                requests,
                usersRecommended
        );
    }
}

