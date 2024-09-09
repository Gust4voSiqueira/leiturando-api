package br.com.leiturando.service.game;

import br.com.leiturando.controller.request.connectwords.FinallyConnectWordsRequest;
import br.com.leiturando.controller.request.connectwords.ConnectWordsRequest;
import br.com.leiturando.controller.response.GameResponse;
import br.com.leiturando.controller.response.connectwords.FinallyConnectWordsResponse;
import br.com.leiturando.entity.User;
import br.com.leiturando.mapper.ConnectWordsMapper;
import br.com.leiturando.repository.UserRepository;
import br.com.leiturando.repository.WordsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static br.com.leiturando.domain.Const.SCORE_TO_CONNECT_WORDS;

@Service
@RequiredArgsConstructor
public class CorrectConnectWordsService {
    WordsRepository wordsRepository;
    UserRepository userRepository;
    ConnectWordsMapper connectWordsMapper;

    public FinallyConnectWordsResponse finallyConnectWords(String email, List<FinallyConnectWordsRequest> wordsRequest) {
        User user = userRepository.findByEmail(email);

        List<ConnectWordsRequest> correctConnectWords = wordsRequest.stream()
                .map(word -> ConnectWordsRequest.builder()
                            .word(wordsRepository.findById(word.getIdWord()).get())
                            .response(word.getResponse())
                            .build()
                ).toList();

        List<ConnectWordsRequest> correctWords = filterCorrectWords(correctConnectWords);
        List<ConnectWordsRequest> incorrectWords = filterIncorrectWords(correctConnectWords);

        int score = correctWords.size() * SCORE_TO_CONNECT_WORDS;
        user.updateUserStatistics(correctWords.size(), incorrectWords.size(), score);

        userRepository.save(user);

        List<GameResponse> correctResponse = convertWordToResponse(correctWords, true);
        List<GameResponse> incorrectResponse = convertWordToResponse(incorrectWords, false);

        List<GameResponse> wordsResponse = Stream.concat(correctResponse.stream(), incorrectResponse.stream())
                .toList();

        return connectWordsMapper.finallyConnectWords(wordsResponse, user, score);
    }

    private List<ConnectWordsRequest> filterCorrectWords(List<ConnectWordsRequest> responses) {
        return responses.stream()
                .filter(wordResponse -> wordResponse.getWord().getWord().equals(wordResponse.getResponse()))
                .toList();
    }

    private List<ConnectWordsRequest> filterIncorrectWords(List<ConnectWordsRequest> responses) {
        return responses.stream()
                .filter(wordResponse -> !wordResponse.getWord().getWord().equals(wordResponse.getResponse()))
                .toList();
    }

    private List<GameResponse> convertWordToResponse(List<ConnectWordsRequest> words, boolean isCorrect) {
        return words.stream()
                .map(word -> connectWordsMapper.finallyConnectWordsResponse(word.getResponse(), isCorrect))
                .toList();
    }
}
