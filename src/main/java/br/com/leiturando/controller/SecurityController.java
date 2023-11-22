package br.com.leiturando.controller;

import br.com.leiturando.controller.request.user.UpdateUserRequest;
import br.com.leiturando.controller.response.user.MyUserResponse;
import br.com.leiturando.domain.Const;
import br.com.leiturando.entity.User;
import br.com.leiturando.service.MyUserService;
import com.amazonaws.services.pinpoint.model.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


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
        }
    }

    @PutMapping("/edit-profile")
    public ResponseEntity<MyUserResponse> editProfile(@Valid @RequestBody UpdateUserRequest userRequest) {
        try {
            User user = (User) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();

            return myUserService.editProfile(user.getEmail(), userRequest);
        } catch (NullPointerException | BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}