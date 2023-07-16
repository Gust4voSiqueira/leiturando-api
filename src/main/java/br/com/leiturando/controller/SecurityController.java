package br.com.leiturando.controller;

import br.com.leiturando.controller.response.MyUserResponse;
import br.com.leiturando.domain.Const;
import br.com.leiturando.entity.User;
import br.com.leiturando.service.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
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
}