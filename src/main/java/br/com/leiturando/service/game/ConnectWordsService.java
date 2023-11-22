package br.com.leiturando.service.game;

import br.com.leiturando.controller.response.connectwords.ListConnectWordsResponse;
import br.com.leiturando.entity.Words;
import br.com.leiturando.mapper.ConnectWordsMapper;
import br.com.leiturando.repository.WordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ConnectWordsService {
    @Autowired
    WordsRepository wordsRepository;

    @Autowired
    ConnectWordsMapper connectWordsMapper;

    public List<ListConnectWordsResponse[]> getWordsToConnect() {
        List<Words> wordsList = wordsRepository.randWordsToConnect();
        return divideArray(wordsList.stream().map(word -> connectWordsMapper.wordToResponse(word)).toArray(ListConnectWordsResponse[]::new), 4);
    }

    public static List<ListConnectWordsResponse[]> divideArray(ListConnectWordsResponse[] array, int size) {
        List<ListConnectWordsResponse[]> results = new ArrayList<>();

        for (int i = 0; i < array.length; i += size) {
            ListConnectWordsResponse[] subArray = Arrays.copyOfRange(array, i, Math.min(i + size, array.length));
            results.add(subArray);
        }

        return results;
    }
}
