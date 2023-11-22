package br.com.leiturando.service.game;

import br.com.leiturando.controller.request.math.FinallyMathRequest;
import br.com.leiturando.controller.response.GameResponse;
import br.com.leiturando.controller.response.math.FinallyMathResponse;
import br.com.leiturando.domain.OperationsMath;
import br.com.leiturando.entity.User;
import br.com.leiturando.mapper.MathMapper;
import br.com.leiturando.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static br.com.leiturando.domain.Const.SCORE_MATH;

@Service
public class CorrectMathService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    MathMapper mathMapper;

    public FinallyMathResponse correctMath(String email, List<FinallyMathRequest> request) {
        User user = userRepository.findByEmail(email);
        List<GameResponse> mathResponses = request.stream()
                .map(this::conferResponse)
                .collect(Collectors.toList());

        int wrong = (int) mathResponses
                .stream().filter(math -> !math.isCorrect()).count();

        int corrects = (int) mathResponses
                .stream().filter(GameResponse::isCorrect).count();

        int score = corrects * SCORE_MATH;
        user.updateUserStatistics(corrects, wrong, score);
        userRepository.save(user);

        return mathMapper.mathToFinally(mathResponses, user, score);
    }

    private GameResponse conferResponse(FinallyMathRequest request) {
        var result = 0;
        var operation = "";

        if(request.getOperation().equals(OperationsMath.ADDITION)) {
            result = request.getNumber1() + request.getNumber2();
            operation = " + ";
        }
        else if(request.getOperation().equals(OperationsMath.MULTIPLICATION)) {
            result = request.getNumber1() * request.getNumber2();
            operation = " x ";
        }
        else if(request.getOperation().equals(OperationsMath.DIVISION)) {
            result = request.getNumber1() / request.getNumber2();
            operation = " / ";
        }
        else if(request.getOperation().equals(OperationsMath.SUBTRACTION)) {
            result = request.getNumber1() - request.getNumber2();
            operation = " - ";
        }

        var content = request.getNumber1() + operation + request.getNumber2() + " = " + request.getResponse();

        return mathMapper.mathToMathResponse(
                content,
                request.getResponse().equals(result)
        );
    }
}
