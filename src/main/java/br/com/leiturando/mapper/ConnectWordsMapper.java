package br.com.leiturando.mapper;

import br.com.leiturando.controller.response.GameResponse;
import br.com.leiturando.controller.response.connectwords.FinallyConnectWordsResponse;
import br.com.leiturando.controller.response.connectwords.ListConnectWordsResponse;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.Words;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConnectWordsMapper {
    public ListConnectWordsResponse wordToResponse(Words words) {
        return ListConnectWordsResponse.builder()
                .id(words.getId())
                .word(words.getWord())
                .build();
    }

    public GameResponse finallyConnectWordsResponse(String words, boolean isCorrect) {
        return GameResponse.builder()
                .content(words)
                .isCorrect(isCorrect)
                .build();
    }

    public FinallyConnectWordsResponse finallyConnectWords(List<GameResponse> words, User user, Integer score) {
        return FinallyConnectWordsResponse.builder()
                .words(words)
                .level(user.getLevel())
                .wrong(user.getWrong())
                .matches(user.getMatches())
                .correct(user.getCorrect())
                .breakthrough(user.getBreakthrough())
                .score(score)
                .build();
    }
}
