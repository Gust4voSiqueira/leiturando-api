package br.com.leiturando.mapper;

import br.com.leiturando.controller.response.GameResponse;
import br.com.leiturando.controller.response.math.FinallyMathResponse;
import br.com.leiturando.controller.response.math.ListMathResponse;
import br.com.leiturando.domain.OperationsMath;
import br.com.leiturando.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MathMapper {
    public ListMathResponse mathToListMathResponse(Integer number1, Integer number2, OperationsMath operation) {
        return ListMathResponse.builder()
                .number1(number1)
                .number2(number2)
                .operation(operation)
                .build();
    }

    public GameResponse mathToMathResponse(String content, boolean isCorrect) {
        return new GameResponse(content, isCorrect);
    }

    public FinallyMathResponse mathToFinally(List<GameResponse> response, User user, Integer score) {
        return FinallyMathResponse
                .builder()
                .results(response)
                .wrong(user.getWrong())
                .level(user.getLevel())
                .matches(user.getMatches())
                .breakthrough(user.getBreakthrough())
                .correct(user.getCorrect())
                .score(score)
                .build();
    }
}
