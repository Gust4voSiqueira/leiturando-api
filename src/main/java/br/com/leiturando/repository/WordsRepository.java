package br.com.leiturando.repository;


import br.com.leiturando.entity.Words;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WordsRepository extends JpaRepository<Words, Long> {
    @Query(value = "SELECT * FROM words ORDER BY RANDOM() LIMIT 7", nativeQuery = true)
    List<Words> randWords();

    @Query(value = "SELECT * FROM words ORDER BY RANDOM() LIMIT 20", nativeQuery = true)
    List<Words> randWordsToConnect();
}
