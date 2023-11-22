package br.com.leiturando.controller;

import br.com.leiturando.controller.request.connectwords.FinallyConnectWordsRequest;
import br.com.leiturando.controller.response.connectwords.FinallyConnectWordsResponse;
import br.com.leiturando.controller.response.connectwords.ListConnectWordsResponse;
import br.com.leiturando.entity.User;
import br.com.leiturando.service.game.ConnectWordsService;
import br.com.leiturando.service.game.CorrectConnectWordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/connectWords")
public class ConnectWordsController {
    @Autowired
    ConnectWordsService connectWordsService;

    @Autowired
    CorrectConnectWordsService correctConnectWordsService;

    @GetMapping
    public List<ListConnectWordsResponse[]> getWordsToConnect() {
        return connectWordsService.getWordsToConnect();
    }

    @PostMapping
    public FinallyConnectWordsResponse correctConnectWords(@Valid @RequestBody List<FinallyConnectWordsRequest> wordsRequest) {
        try {
            User user = (User) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();
            return correctConnectWordsService.finallyConnectWords(user.getEmail(), wordsRequest);
        } catch (NullPointerException e) {
            throw new NullPointerException("Você precisa de um token para acessar.");
        }

    }
}
