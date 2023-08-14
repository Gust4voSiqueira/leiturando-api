package br.com.leiturando.controller;

import br.com.leiturando.controller.request.RegisterUserRequest;
import br.com.leiturando.controller.response.ErrorResponse;
import br.com.leiturando.controller.response.RegisterUserResponse;
import br.com.leiturando.exception.UserExistsException;
import br.com.leiturando.service.RegisterUserService;
import com.amazonaws.services.ecr.model.ImageNotFoundException;
import com.amazonaws.services.pinpoint.model.BadRequestException;
import com.amazonaws.services.pinpoint.model.InternalServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    RegisterUserService registerUserService;

    @PostMapping(value = "/register")
    public RegisterUserResponse registerUser(@RequestBody RegisterUserRequest registerUserRequest) {
        try {
            return registerUserService.registerService(registerUserRequest);
        } catch (UserExistsException | ImageNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            throw new BadRequestException(errorResponse.getMessage());
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            throw new InternalServerErrorException(errorResponse.getMessage());
        }
    }
}