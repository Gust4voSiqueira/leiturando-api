package br.com.leiturando.service;

import br.com.leiturando.controller.request.user.RegisterUserRequest;
import br.com.leiturando.entity.User;
import br.com.leiturando.mapper.RegisterUserMapper;
import br.com.leiturando.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class RegisterUserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RegisterUserMapper registerUserMapper;

    public ResponseEntity<String> registerService(RegisterUserRequest userRequest) {
        User existingUser = userRepository.findByEmail(userRequest.getEmail());
        if (existingUser != null) {
            return new ResponseEntity<>("Já existe um usuário com este e-mail.", HttpStatus.CONFLICT);
        }

        if(!Objects.equals(userRequest.getConfirmPassword(), userRequest.getPassword())) {
            return new ResponseEntity<>("As senhas são diferentes.", HttpStatus.BAD_REQUEST);
        }

        String imageUser;
        if(userRequest.getCharacterName() != null) {
            imageUser = userRequest.getCharacterName();
        } else {
            imageUser = "NoImage";
        }

        User userSave = createUser(userRequest, imageUser);
        userRepository.save(userSave);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private User createUser(RegisterUserRequest userRequest, String file) {
        return registerUserMapper.requestToUser(
                userRequest,
                file,
                passwordEncoder.encode(userRequest.getPassword()));
    }
}
