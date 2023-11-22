package br.com.leiturando.service.game;

import br.com.leiturando.controller.response.connectwords.ListConnectWordsResponse;
import br.com.leiturando.controller.response.connectwords.ListConnectWordsResponseTest;
import br.com.leiturando.entity.Words;
import br.com.leiturando.entity.WordsTest;
import br.com.leiturando.mapper.ConnectWordsMapper;
import br.com.leiturando.repository.WordsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConnectWordsServiceTest {
    @InjectMocks
    ConnectWordsService connectWordsService;

    @Mock
    WordsRepository wordsRepository;
    @Mock
    ConnectWordsMapper connectWordsMapper;
    Words words;
    ListConnectWordsResponse listConnectWordsResponse;

    @BeforeEach
    public void init() {
        words = WordsTest.builderWords();
        listConnectWordsResponse = ListConnectWordsResponseTest.builderListConnectWordsResponse();
    }

    @Test
    void getWordsToConnectCorrectly() {
        when(wordsRepository.randWordsToConnect()).thenReturn(List.of(words));
        when(connectWordsMapper.wordToResponse(words)).thenReturn(listConnectWordsResponse);

        var result = connectWordsService.getWordsToConnect();

        Assertions.assertNotNull(result);
    }
}
