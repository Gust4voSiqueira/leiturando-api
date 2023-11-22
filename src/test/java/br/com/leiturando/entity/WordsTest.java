package br.com.leiturando.entity;

import br.com.leiturando.Consts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WordsTest {
    public static Words builderWords() {
        return Words.builder()
                .id(1L)
                .word(Consts.WORD_DEFAULT)
                .build();
    }

    @Test
    void buildWordsCorrectly(){
        var result = builderWords();

        Assertions.assertNotNull(result.getId());
        Assertions.assertNotNull(result.getWord());
    }
}
