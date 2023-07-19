package br.com.leiturando.service;

import br.com.leiturando.controller.response.MyUserResponse;
import br.com.leiturando.entity.User;
import br.com.leiturando.mapper.MyUserMapper;
import br.com.leiturando.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static br.com.leiturando.domain.Const.CHARACTERS_LIST;

@Service
public class MyUserService {
    @Autowired
    MyUserMapper myUserMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FileService fileService;

    public MyUserResponse myUserService(String email) {
        User user = userRepository.findByEmail(email);

        String image;
        if(CHARACTERS_LIST.contains(user.getImageUrl())) {
            image = fileService.downloadFile(user.getImageUrl() + ".png");
        } else {
            image = fileService.downloadFile(user.getImageUrl());
        }

        return myUserMapper.myUserDtoToResponse(user, image);
    }
}
