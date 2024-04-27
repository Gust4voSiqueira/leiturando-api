package br.com.leiturando.service;

import br.com.leiturando.controller.request.user.UpdateUserRequest;
import br.com.leiturando.controller.response.user.MyUserResponse;
import br.com.leiturando.controller.response.user.RankingResponse;
import br.com.leiturando.entity.User;
import br.com.leiturando.mapper.MyUserMapper;
import br.com.leiturando.repository.FriendshipRepository;
import br.com.leiturando.repository.UserRepository;
import com.amazonaws.services.pinpoint.model.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyUserService {
    @Autowired
    MyUserMapper myUserMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    FriendshipRepository friendshipRepository;

    public MyUserResponse myUserService(String email) {
        User user = userRepository.findByEmail(email);

        return myUserMapper.myUserDtoToResponse(user);
    }

    public ResponseEntity<MyUserResponse> editProfile(String email, UpdateUserRequest userRequest) {
        User user = userRepository.findByEmail(email);

        if(userRequest.getNewPassword() != null && !userRequest.getNewPassword().equals(userRequest.getConfirmNewPassword())) {
            throw new BadRequestException("As senhas não conferem.");
        }

        User userUpdated = updateUserFields(user, userRequest);

        userRepository.save(userUpdated);

        return ResponseEntity.ok().body(myUserMapper.myUserDtoToResponse(userUpdated));
    }

    public RankingResponse getGlobalRanking() {
        List<User> globalRanking = userRepository.findFirst5ByOrderByLevelDesc();

        List<String> fields = globalRanking.stream()
                .map(userField -> getField(userField.getName()))
                .collect(Collectors.toList());

        List<Integer> data = globalRanking.stream().map(User::getLevel).collect(Collectors.toList());

        return myUserMapper.userToRankingResponse(fields, data);
    }

    public RankingResponse getFriendsRanking(String email) {
        User user = userRepository.findByEmail(email);

        List<User> friendRanking = friendshipRepository.findFirst4UsersAndFriendsOrderByLevelDesc(user, PageRequest.of(0, 4))
                .stream()
                .map(friendship -> friendship.getUser().equals(user) ? friendship.getFriend() : friendship.getUser())
                .collect(Collectors.toList());

        friendRanking.add(user);

        List<String> fields = friendRanking.stream()
                .map(userField -> getField(userField.getName()))
            .collect(Collectors.toList());

        List<Integer> data = friendRanking.stream().map(User::getLevel).collect(Collectors.toList());

        return myUserMapper.userToRankingResponse(fields, data);
    }

    private User updateUserFields(User user, UpdateUserRequest userRequest) {
        if (userRequest.getName() != null && !userRequest.getName().equals("")) {
            user.setName(userRequest.getName());
        }

        if (userRequest.getCharacterName() != null && !userRequest.getCharacterName().equals("")) {
            user.setImage(userRequest.getCharacterName());
        }

        if (userRequest.getDateOfBirth() != null) {
            user.setDateOfBirth(userRequest.getDateOfBirth());
        }

        if (userRequest.getNewPassword() != null && !userRequest.getNewPassword().equals("")) {
            user.setPassword(passwordEncoder.encode(userRequest.getNewPassword()));
        }

        return user;
    }

    private String getField(String name) {
        int maxNameLength = 5;

        if(name.length() < maxNameLength) {
            return name;
        }

        return name.substring(0, maxNameLength);
    }
}
