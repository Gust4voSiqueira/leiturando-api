package br.com.leiturando.service.game;

import br.com.leiturando.controller.request.words.FinallyWordsRequest;
import br.com.leiturando.controller.response.GameResponse;
import br.com.leiturando.controller.response.words.FinallyWordsResponse;
import br.com.leiturando.controller.response.words.ListWordsResponse;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.Words;
import br.com.leiturando.mapper.WordsMapper;
import br.com.leiturando.repository.UserRepository;
import br.com.leiturando.repository.WordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static br.com.leiturando.domain.Const.SCORE_WORDS;

@Service
public class WordsService {
    @Autowired
    WordsRepository wordsRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WordsMapper wordsMapper;


    public List<ListWordsResponse> getWords() {
        List<Words> wordsList = wordsRepository.randWords();

        return wordsList.stream().map(word -> wordsMapper.wordsToResponse(word))
                .collect(Collectors.toList());
    }

    public FinallyWordsResponse finallyWordsService(String email, FinallyWordsRequest request) {
        User user = userRepository.findByEmail(email);

        List<Optional<Words>> wordsDatabase = getWordsFromDatabase(request.getWordIds());

        List<String> correctWords = filterCorrectWords(wordsDatabase, request.getResponses());
        List<String> incorrectWords = filterIncorrectWords(wordsDatabase, request.getResponses());

        int score = correctWords.size() * SCORE_WORDS;
        user.updateUserStatistics(correctWords.size(), incorrectWords.size(), score);

        userRepository.save(user);

        List<GameResponse> correctResponse = convertWordToResponse(correctWords, true);
        List<GameResponse> incorrectResponse = convertWordToResponse(incorrectWords, false);

        List<GameResponse> wordsResponse = Stream.concat(correctResponse.stream(), incorrectResponse.stream())
                .collect(Collectors.toList());

        return wordsMapper.finallyWords(wordsResponse, user, score);
    }

    private List<GameResponse> convertWordToResponse(List<String> words, boolean isCorrect) {
        return words.stream()
                .map(word -> wordsMapper.createWordsResponse(word, isCorrect))
                .collect(Collectors.toList());
    }

    private List<Optional<Words>> getWordsFromDatabase(List<Long> wordIds) {
        return wordIds.stream()
                .map(word -> wordsRepository.findById(word))
                .collect(Collectors.toList());
    }

    private List<String> filterCorrectWords(List<Optional<Words>> wordsDatabase, List<String> responses) {
        var responsesLowerCase = responses.stream().map(String::toLowerCase).collect(Collectors.toList());

        return wordsDatabase.stream()
                .map(word -> word.map(Words::getWord).orElse(null))
                .filter(Objects::nonNull)
                .filter(word -> responsesLowerCase.contains(word.toLowerCase()))
                .collect(Collectors.toList());
    }

    private List<String> filterIncorrectWords(List<Optional<Words>> wordsDatabase, List<String> wordsFiltered) {
        var responsesLowerCase = wordsFiltered.stream().map(String::toLowerCase).collect(Collectors.toList());

        return wordsDatabase.stream()
                .map(word -> word.map(Words::getWord).orElse(null))
                .filter(Objects::nonNull)
                .filter(word -> !responsesLowerCase.contains(word))
                .collect(Collectors.toList());
    }

}
