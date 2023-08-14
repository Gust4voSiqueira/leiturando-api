package br.com.leiturando.service;

import br.com.leiturando.controller.request.UpdateUserRequest;
import br.com.leiturando.controller.response.MyUserResponse;
import br.com.leiturando.entity.User;
import br.com.leiturando.mapper.MyUserMapper;
import br.com.leiturando.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MyUserService {
    @Autowired
    MyUserMapper myUserMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FileService fileService;

    public MyUserResponse myUserService(String email) throws IOException {
        User user = userRepository.findByEmail(email);

        return myUserMapper.myUserDtoToResponse(user);
    }

    public MyUserResponse editProfile(String email, UpdateUserRequest userRequest) {
        User user = userRepository.findByEmail(email);

        User userUpdated = updateUserFields(user, userRequest);

        userRepository.save(userUpdated);

        return myUserMapper.myUserDtoToResponse(userUpdated);
    }

    private User updateUserFields(User user, UpdateUserRequest userRequest) {
        User updatedUser = new User();

        if (userRequest.getEmail() != null) {
            updatedUser.setEmail(userRequest.getEmail());
        } else {
            updatedUser.setEmail(user.getEmail());
        }

        if (userRequest.getName() != null) {
            updatedUser.setName(userRequest.getName());
        } else {
            updatedUser.setName(user.getName());
        }

        if (userRequest.getCharacterName() != null) {
            updatedUser.setImage(userRequest.getCharacterName());
        } else {
            updatedUser.setImage(user.getImage());
        }

        if (userRequest.getDateOfBirth() != null) {
            updatedUser.setDateOfBirth(userRequest.getDateOfBirth());
        } else {
            updatedUser.setDateOfBirth(user.getDateOfBirth());
        }

        updatedUser.setId(user.getId());
        updatedUser.setLevel(user.getLevel());
        updatedUser.setBreakthrough(user.getBreakthrough());
        updatedUser.setCreatedAt(user.getCreatedAt());
        updatedUser.setMatches(user.getMatches());
        updatedUser.setCorrect(user.getCorrect());
        updatedUser.setWrong(user.getWrong());
        updatedUser.setPassword(user.getPassword());

        return updatedUser;
    }
}
