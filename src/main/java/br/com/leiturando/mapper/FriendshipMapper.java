package br.com.leiturando.mapper;

import br.com.leiturando.entity.FriendRequests;
import br.com.leiturando.entity.Friendship;
import org.springframework.stereotype.Component;

@Component
public class FriendshipMapper {

    public Friendship requestToFriendship(FriendRequests request) {
        return Friendship
                .builder()
                .user(request.getRequested())
                .friend(request.getRequester())
                .build();
    }
}
