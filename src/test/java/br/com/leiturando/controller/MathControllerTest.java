package br.com.leiturando.controller;

import br.com.leiturando.BaseAuthTest;
import br.com.leiturando.controller.request.math.FinallyMathRequest;
import br.com.leiturando.controller.request.math.FinallyMathRequestTest;
import br.com.leiturando.controller.response.math.FinallyMathResponse;
import br.com.leiturando.controller.response.math.FinallyMathResponseTest;
import br.com.leiturando.controller.response.math.ListMathResponse;
import br.com.leiturando.controller.response.math.ListMathResponseTest;
import br.com.leiturando.domain.OperationsMath;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import br.com.leiturando.service.game.CorrectMathService;
import br.com.leiturando.service.game.MathService;
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
class MathControllerTest extends BaseAuthTest {
    @InjectMocks
    MathController mathController;

    @Mock
    MathService mathService;

    @Mock
    CorrectMathService correctMathService;

    User user;
    OperationsMath operations;
    ListMathResponse listMathResponse;
    FinallyMathRequest finallyMathRequest;
    FinallyMathResponse finallyMathResponse;

    @BeforeEach
    public void init() {
        user = UserTest.builderUser();
        operations = OperationsMath.ADDITION;
        listMathResponse = ListMathResponseTest.builderListMathResponse();
        finallyMathRequest = FinallyMathRequestTest.builderFinallyMathRequest();
        finallyMathResponse = FinallyMathResponseTest.builderFinallyMathResponse();
    }

    @Test
    void getMathCorrectly() {
        when(mathService.getMath(user.getEmail(), List.of(operations))).thenReturn(List.of(listMathResponse));

        var result = mathController.getMath(List.of(operations));

        Assertions.assertEquals(List.of(listMathResponse), result);
    }

    @Test
    void correctMathCorrectly() {
        when(correctMathService.correctMath(user.getEmail(), List.of(finallyMathRequest))).thenReturn(finallyMathResponse);

        var result = mathController.correctMath(List.of(finallyMathRequest));

        Assertions.assertEquals(finallyMathResponse, result);
    }
}
