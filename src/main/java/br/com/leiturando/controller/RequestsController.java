package br.com.leiturando.controller;

import br.com.leiturando.controller.response.ErrorResponse;
import br.com.leiturando.controller.response.SendRequestResponse;
import br.com.leiturando.entity.User;
import br.com.leiturando.service.RequestsService;
import com.amazonaws.services.kms.model.NotFoundException;
import com.amazonaws.services.pinpoint.model.BadRequestException;
import com.amazonaws.services.pinpoint.model.InternalServerErrorException;
import org.hibernate.procedure.ParameterStrategyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/request")
public class RequestsController {
    @Autowired
    RequestsService requestsService;

    @PostMapping("/send/{requestedId}")
    public SendRequestResponse sendRequest(@PathVariable Long requestedId) throws Exception {
        try {
            User user = (User) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();
            return requestsService.sendRequest(user.getEmail(), requestedId);
        } catch (NotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            throw new NotFoundException(errorResponse.getMessage());
        } catch (ParameterStrategyException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            throw new BadRequestException(errorResponse.getMessage());
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            throw new InternalServerErrorException(errorResponse.getMessage());
        }
    }
}
