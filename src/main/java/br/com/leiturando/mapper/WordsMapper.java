package br.com.leiturando.mapper;

import br.com.leiturando.controller.response.GameResponse;
import br.com.leiturando.controller.response.words.FinallyWordsResponse;
import br.com.leiturando.controller.response.words.ListWordsResponse;

import br.com.leiturando.entity.User;
import br.com.leiturando.entity.Words;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WordsMapper {
    public ListWordsResponse wordsToResponse(Words words) {
        return ListWordsResponse.builder()
                .id(words.getId())
                .word(words.getWord())
                .build();
    }

    public FinallyWordsResponse finallyWords(List<GameResponse> words, User user, Integer score) {
        return FinallyWordsResponse.builder()
                .words(words)
                .level(user.getLevel())
                .wrong(user.getWrong())
                .matches(user.getMatches())
                .correct(user.getCorrect())
                .breakthrough(user.getBreakthrough())
                .score(score)
                .build();
    }

    public GameResponse createWordsResponse(String word, boolean isCorrect) {
        return GameResponse.builder()
                .content(word)
                .isCorrect(isCorrect)
                .build();
    }
}
