package br.com.leiturando.service;

import br.com.leiturando.controller.request.RegisterUserRequest;
import br.com.leiturando.controller.response.RegisterUserResponse;
import br.com.leiturando.domain.Const;
import br.com.leiturando.entity.Role;
import br.com.leiturando.entity.User;
import br.com.leiturando.exception.UserExistsException;
import br.com.leiturando.mapper.RegisterUserMapper;
import br.com.leiturando.repository.RoleRepository;
import br.com.leiturando.repository.UserRepository;
import com.amazonaws.services.ecr.model.ImageNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.openssl.PasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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


    public RegisterUserResponse registerService(RegisterUserRequest userRequest) throws Exception {
        User existingUser = userRepository.findByEmail(userRequest.getEmail());
        if (existingUser != null) {
            throw new UserExistsException("Já existe um usuário com este e-mail.");
        }

        if(!Objects.equals(userRequest.getConfirmPassword(), userRequest.getPassword())) {
            throw new PasswordException("As senhas são diferentes.");
        }

        String imageUser;
        if(userRequest.getCharacterName() != null) {
            imageUser = userRequest.getCharacterName();
        } else {
            imageUser = "NoImage";
        }

        User userSave = createUser(userRequest, imageUser);
        userRepository.save(userSave);

        return registerUserMapper.userToRequest(userSave);
    }

    private User createUser(RegisterUserRequest userRequest, String file) {
        return registerUserMapper.requestToUser(
                userRequest,
                file,
                passwordEncoder.encode(userRequest.getPassword()));
    }
}
