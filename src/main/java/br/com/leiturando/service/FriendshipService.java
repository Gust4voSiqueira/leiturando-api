package br.com.leiturando.service;

import br.com.leiturando.controller.response.UserResponse;
import br.com.leiturando.entity.Friendship;
import br.com.leiturando.entity.User;
import br.com.leiturando.mapper.UserMapper;
import br.com.leiturando.repository.FriendshipRepository;
import br.com.leiturando.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendshipService {
    @Autowired
    FriendshipRepository friendshipRepository;

    @Autowired
    UserMapper userMapper;

    public List<UserResponse> listFriendship(User user) {
        List<Friendship> friendshipMyUser = friendshipRepository.findAllByUserOrFriend(user);

        var teste = friendshipMyUser
                .stream()
                .map(friendship -> {
                    if(friendship.getUser() != user) {
                        return friendship.getUser();
                    }

                    return friendship.getFriend();
                })
                .filter(friendshipUser -> friendshipUser != user).collect(Collectors.toList());

        return teste.stream()
                .map(friendship -> userMapper.userToResponse(friendship))
                .collect(Collectors.toList());
    }
}
