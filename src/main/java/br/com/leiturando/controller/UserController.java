package br.com.leiturando.controller;

import br.com.leiturando.controller.request.user.RegisterUserRequest;
import br.com.leiturando.controller.response.ErrorResponse;
import br.com.leiturando.controller.response.user.RankingResponse;
import br.com.leiturando.controller.response.user.SearchUserResponse;
import br.com.leiturando.entity.User;
import br.com.leiturando.service.FriendshipService;
import br.com.leiturando.service.MyUserService;
import br.com.leiturando.service.RegisterUserService;
import com.amazonaws.services.pinpoint.model.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    RegisterUserService registerUserService;

    @Autowired
    FriendshipService friendshipService;

    @Autowired
    MyUserService myUserService;

    @PostMapping(value = "/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterUserRequest registerUserRequest) {
        try {
            return registerUserService.registerService(registerUserRequest);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            throw new BadRequestException(errorResponse.getMessage());
        }
    }

    @GetMapping("/searchUsers/{filter}")
    public List<SearchUserResponse> searchUsers(@PathVariable(value = "filter") String filter) {
        try {
            User user = (User) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();

            return friendshipService.searchUsers(filter, user.getEmail());
        } catch (BadRequestException e) {
            throw new BadRequestException("Falha ao buscar usuários.");
        }
    }

    @DeleteMapping("/unfriend/{idFriend}")
    public void unfriend(@PathVariable(value = "idFriend") Long idFriend) {
        try {
            User user = (User) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();

            friendshipService.unFriend(user.getEmail(), idFriend);
        } catch (BadRequestException e) {
            throw new BadRequestException("Falha ao desfazer amizade.");
        }
    }

    @GetMapping("/globalRanking")
    public RankingResponse getGlobalRanking() {
        try {
            return myUserService.getGlobalRanking();
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @GetMapping("/friendsRanking")
    public RankingResponse getFriendsRanking() {
        try {
            User user = (User) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();

            return myUserService.getFriendsRanking(user.getEmail());
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}