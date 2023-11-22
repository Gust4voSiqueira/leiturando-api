package br.com.leiturando.mapper;

import br.com.leiturando.controller.response.GameResponse;
import br.com.leiturando.controller.response.math.FinallyMathResponse;
import br.com.leiturando.controller.response.math.FinallyMathResponseTest;
import br.com.leiturando.controller.response.math.ListMathResponse;
import br.com.leiturando.controller.response.math.ListMathResponseTest;
import br.com.leiturando.domain.OperationsMath;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class MathMapperTest {
    @InjectMocks
    MathMapper mathMapper;

    User user;
    ListMathResponse listMathResponse;
    GameResponse gameResponse;
    FinallyMathResponse finallyMathResponse;

    @BeforeEach
    public void init() {
        user = UserTest.builderUser();
        listMathResponse = ListMathResponseTest.builderListMathResponse();
        gameResponse = GameResponse
                .builder()
                .content("1 + 2 = 3")
                .isCorrect(true)
                .build();
        finallyMathResponse = FinallyMathResponseTest.builderFinallyMathResponse();
    }

    @Test
    void setMathToListMathResponse() {
        var result = mathMapper.mathToListMathResponse(1, 1, OperationsMath.ADDITION);

        Assertions.assertEquals(listMathResponse.getNumber1(), result.getNumber1());
        Assertions.assertEquals(listMathResponse.getNumber2(), result.getNumber2());
        Assertions.assertEquals(listMathResponse.getOperation(), result.getOperation());
    }

    @Test
    void setMathToMathResponse() {
        var result = mathMapper.mathToMathResponse("1 + 2 = 3", true);

        Assertions.assertEquals(gameResponse.getContent(), result.getContent());
        Assertions.assertEquals(gameResponse.isCorrect(), result.isCorrect());
    }

    @Test
    void setMathToFinally() {
        var result = mathMapper.mathToFinally(List.of(gameResponse), user, 5);

        Assertions.assertEquals(finallyMathResponse.getResults().get(0).getContent(), result.getResults().get(0).getContent());
        Assertions.assertEquals(finallyMathResponse.getWrong(), result.getWrong());
        Assertions.assertEquals(finallyMathResponse.getLevel(), result.getLevel());
        Assertions.assertEquals(finallyMathResponse.getMatches(), result.getMatches());
        Assertions.assertEquals(finallyMathResponse.getBreakthrough(), result.getBreakthrough());
        Assertions.assertEquals(finallyMathResponse.getCorrect(), result.getCorrect());
        Assertions.assertEquals(finallyMathResponse.getScore(), result.getScore());
    }
}
