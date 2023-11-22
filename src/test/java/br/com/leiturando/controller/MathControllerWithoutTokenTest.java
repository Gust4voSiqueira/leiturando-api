package br.com.leiturando.controller;

import br.com.leiturando.controller.request.math.FinallyMathRequest;
import br.com.leiturando.controller.request.math.FinallyMathRequestTest;
import br.com.leiturando.domain.OperationsMath;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;


@ExtendWith(MockitoExtension.class)
class MathControllerWithoutTokenTest {
    @InjectMocks
    MathController mathController;

    List<OperationsMath> operations;
    List<FinallyMathRequest> finallyMathRequest;

    @BeforeEach
    public void init() {
        operations = List.of(OperationsMath.ADDITION);
        finallyMathRequest = List.of(FinallyMathRequestTest.builderFinallyMathRequest());
    }

    @Test
    void getMathFailedToken() {
        Assert.assertThrows(NullPointerException.class, () -> mathController.getMath(operations));
    }

    @Test
    void correctMathFailedToken() {
        Assert.assertThrows(NullPointerException.class, () -> mathController.correctMath(finallyMathRequest));
    }
}
