package br.com.leiturando.controller;

import br.com.leiturando.controller.request.UpdateUserRequest;
import br.com.leiturando.controller.response.MyUserResponse;
import br.com.leiturando.domain.Const;
import br.com.leiturando.entity.User;
import br.com.leiturando.service.MyUserService;
import com.amazonaws.services.pinpoint.model.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RestController
public class SecurityController {
    @Autowired
    MyUserService myUserService;

    @GetMapping(value = "/user-auth")
    @ResponseBody
    @Secured({Const.ROLE_CLIENT, Const.ROLE_ADMIN})
    public MyUserResponse user() {
        try {
            User user = (User) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();

            return myUserService.myUserService(user.getEmail());
        } catch (NullPointerException e) {
            throw new NullPointerException("Você precisa de um token para acessar.");
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @PutMapping("/edit-profile")
    public MyUserResponse editProfile(@RequestBody UpdateUserRequest userRequest) {
        try {
            User user = (User) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();

            return myUserService.editProfile(user.getEmail(), userRequest);
        } catch (NullPointerException e) {
            throw new NullPointerException(e.getMessage());
        }
    }
}