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
        if (userRequest.getEmail() != null && !userRequest.getEmail().equals("")) {
            user.setEmail(userRequest.getEmail());
        }

        if (userRequest.getName() != null && !userRequest.getName().equals("")) {
            user.setName(userRequest.getName());
        }

        if (userRequest.getCharacterName() != null && !userRequest.getCharacterName().equals("")) {
            user.setImage(userRequest.getCharacterName());
        }

        if (userRequest.getDateOfBirth() != null) {
            user.setDateOfBirth(userRequest.getDateOfBirth());
        }

        return user;
    }
}
