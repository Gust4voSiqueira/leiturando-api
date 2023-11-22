package br.com.leiturando.controller;

import br.com.leiturando.controller.request.math.FinallyMathRequest;
import br.com.leiturando.controller.response.math.FinallyMathResponse;
import br.com.leiturando.controller.response.math.ListMathResponse;
import br.com.leiturando.domain.OperationsMath;
import br.com.leiturando.entity.User;
import br.com.leiturando.service.game.CorrectMathService;
import br.com.leiturando.service.game.MathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/math")
public class MathController {
    @Autowired
    MathService mathService;

    @Autowired
    CorrectMathService correctMathService;

    @PostMapping("/getMath")
    public List<ListMathResponse> getMath(@Valid @RequestBody List<OperationsMath> operations) {
        try {
            User user = (User) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();

            return mathService.getMath(user.getEmail(), operations);
        } catch (NullPointerException e) {
            throw new NullPointerException("Você precisa de um token para acessar.");
        }
    }

    @PostMapping("/correct")
    public FinallyMathResponse correctMath(@RequestBody List<FinallyMathRequest> mathRequest) {
        try {
            User user = (User) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();
            return correctMathService.correctMath(user.getEmail(), mathRequest);
        } catch (NullPointerException e) {
            throw new NullPointerException("Você precisa de um token para acessar.");
        }
    }
}
