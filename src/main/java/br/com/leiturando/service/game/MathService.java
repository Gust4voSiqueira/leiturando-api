package br.com.leiturando.service.game;

import br.com.leiturando.controller.response.math.ListMathResponse;
import br.com.leiturando.domain.OperationsMath;
import br.com.leiturando.mapper.MathMapper;
import br.com.leiturando.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MathService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    MathMapper mathMapper;

    private final SecureRandom random = new SecureRandom();

    public List<ListMathResponse> getMath(String email, List<OperationsMath> operations) {
        Integer level = userRepository.findByEmail(email).getLevel();
        List<ListMathResponse> response = new ArrayList<>();

        for (var cont = 0; cont < 7; cont++) {
            OperationsMath operation = getOperation(operations);

            response.add(getNumbers(level + 10, operation));
        }

        return response;
    }

    private ListMathResponse getNumbers(Integer limitNumber, OperationsMath operation) {
        int number1 = getNumbers(limitNumber);
        int number2 = getNumbers(limitNumber);

        if(operation.equals(OperationsMath.DIVISION)) {
            while(number1 % number2 != 0) {
                number1 = getNumbers(limitNumber);
                number2 = getNumbers(limitNumber);
            }
        }

        if(Arrays.asList(OperationsMath.SUBTRACTION, OperationsMath.DIVISION).contains(operation) && number1 < number2) {
            return mathMapper.mathToListMathResponse(number2, number1, operation);
        }

        return mathMapper.mathToListMathResponse(number1, number2, operation);
    }

    private OperationsMath getOperation(List<OperationsMath> operations) {
        var operationIndex = random.nextInt(operations.size());

        return operations.get(operationIndex);
    }

    private Integer getNumbers(Integer limitNumber) {
        return random.nextInt(limitNumber) + 1;
    }
}
