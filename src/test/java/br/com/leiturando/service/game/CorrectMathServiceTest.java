package br.com.leiturando.service.game;

import br.com.leiturando.controller.request.math.FinallyMathRequest;
import br.com.leiturando.controller.request.math.FinallyMathRequestTest;
import br.com.leiturando.controller.response.GameResponse;
import br.com.leiturando.controller.response.math.FinallyMathResponse;
import br.com.leiturando.domain.OperationsMath;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import br.com.leiturando.mapper.MathMapper;
import br.com.leiturando.repository.UserRepository;
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
class CorrectMathServiceTest {
    @InjectMocks
    CorrectMathService correctMathService;

    @Mock
    UserRepository userRepository;

    @Mock
    MathMapper mathMapper;

    User user;
    FinallyMathRequest additionRequest;
    FinallyMathRequest subtractionRequest;
    FinallyMathRequest multiplicationRequest;
    FinallyMathRequest divisionRequest;
    GameResponse additionResponse;
    GameResponse subtractionResponse;
    GameResponse multiplicationResponse;
    GameResponse divisionResponse;
    FinallyMathResponse finallyMathResponse;

    @BeforeEach
    public void init() {
        user = UserTest.builderUser();
        additionRequest = FinallyMathRequestTest.builderFinallyMathRequest();
        subtractionRequest = FinallyMathRequest
                .builder()
                .number1(4)
                .number2(1)
                .operation(OperationsMath.SUBTRACTION)
                .response(3)
                .build();
        multiplicationRequest = FinallyMathRequest
                .builder()
                .number1(2)
                .number2(2)
                .operation(OperationsMath.MULTIPLICATION)
                .response(4)
                .build();
        divisionRequest = FinallyMathRequest
                .builder()
                .number1(2)
                .number2(2)
                .operation(OperationsMath.DIVISION)
                .response(1)
                .build();
        additionResponse = GameResponse
                .builder()
                .content("1 + 2 = 3")
                .isCorrect(true)
                .build();
        subtractionResponse = GameResponse
                .builder()
                .content("4 - 1 = 3")
                .isCorrect(true)
                .build();
        multiplicationResponse = GameResponse
                .builder()
                .content("2 x 2 = 4")
                .isCorrect(true)
                .build();
        divisionResponse = GameResponse
                .builder()
                .content("2 / 2 = 1")
                .isCorrect(true)
                .build();
        finallyMathResponse = FinallyMathResponse.builder()
                .level(user.getLevel())
                .breakthrough(user.getBreakthrough())
                .matches(user.getMatches())
                .correct(user.getCorrect())
                .wrong(user.getWrong())
                .score(3)
                .build();
    }

    @Test
    void correctMathAdditionCorrectly() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(mathMapper.mathToMathResponse(
                additionResponse.getContent(),
               true)).thenReturn(additionResponse);

        when(userRepository.save(user)).thenReturn(user);
        when(mathMapper.mathToFinally(List.of(additionResponse), user, 3)).thenReturn(finallyMathResponse);

        finallyMathResponse.setResults(List.of(additionResponse));

        var result = correctMathService.correctMath(user.getEmail(), List.of(additionRequest));

        Assertions.assertEquals(finallyMathResponse, result);
    }

    @Test
    void correctMathSubtractionCorrectly() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(mathMapper.mathToMathResponse(
                subtractionResponse.getContent(),
                true)).thenReturn(subtractionResponse);

        when(userRepository.save(user)).thenReturn(user);
        when(mathMapper.mathToFinally(List.of(subtractionResponse), user, 3)).thenReturn(finallyMathResponse);

        finallyMathResponse.setResults(List.of(subtractionResponse));

        var result = correctMathService.correctMath(user.getEmail(), List.of(subtractionRequest));
        finallyMathResponse.setResults(List.of(subtractionResponse));

        Assertions.assertEquals(finallyMathResponse, result);
    }

    @Test
    void correctMathMultiplicationCorrectly() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(mathMapper.mathToMathResponse(
                multiplicationResponse.getContent(),
                true)).thenReturn(multiplicationResponse);

        when(userRepository.save(user)).thenReturn(user);
        when(mathMapper.mathToFinally(List.of(multiplicationResponse), user, 3)).thenReturn(finallyMathResponse);

        finallyMathResponse.setResults(List.of(multiplicationResponse));

        var result = correctMathService.correctMath(user.getEmail(), List.of(multiplicationRequest));
        finallyMathResponse.setResults(List.of(multiplicationResponse));

        Assertions.assertEquals(finallyMathResponse, result);
    }

    @Test
    void correctMathDivisionCorrectly() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(mathMapper.mathToMathResponse(
                divisionResponse.getContent(),
                true)).thenReturn(divisionResponse);

        when(userRepository.save(user)).thenReturn(user);
        when(mathMapper.mathToFinally(List.of(divisionResponse), user, 3)).thenReturn(finallyMathResponse);

        finallyMathResponse.setResults(List.of(divisionResponse));

        var result = correctMathService.correctMath(user.getEmail(), List.of(divisionRequest));
        finallyMathResponse.setResults(List.of(divisionResponse));

        Assertions.assertEquals(finallyMathResponse, result);
    }
}
