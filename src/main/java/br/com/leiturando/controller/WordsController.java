package br.com.leiturando.controller;

import br.com.leiturando.controller.request.words.FinallyWordsRequest;
import br.com.leiturando.controller.response.words.FinallyWordsResponse;
import br.com.leiturando.controller.response.words.ListWordsResponse;
import br.com.leiturando.entity.User;
import br.com.leiturando.service.game.WordsService;
import com.amazonaws.services.pinpoint.model.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/words")
public class WordsController {
    @Autowired
    WordsService wordsService;

    @GetMapping
    public List<ListWordsResponse> getWords() {
        try {
            return wordsService.getWords();
        } catch (BadRequestException e) {
            throw new BadRequestException("Falha ao buscar por palavras.");
        }
    }

    @PostMapping
    public FinallyWordsResponse correctWords(@Valid @RequestBody FinallyWordsRequest wordsRequest) {
        try {
            User user = (User) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();
            return wordsService.finallyWordsService(user.getEmail(), wordsRequest);
        } catch (NullPointerException e) {
            throw new NullPointerException("Você precisa de um token para acessar.");
        }
    }
}
