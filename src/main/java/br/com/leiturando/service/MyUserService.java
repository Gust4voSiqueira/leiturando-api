package br.com.leiturando.service;

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

        String image = fileService.downloadFile(user.getImageUrl());

        return myUserMapper.myUserDtoToResponse(user, image);
    }
}
