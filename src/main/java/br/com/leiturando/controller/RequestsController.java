package br.com.leiturando.controller;

import br.com.leiturando.controller.response.requests.RequestResponse;
import br.com.leiturando.controller.response.ErrorResponse;
import br.com.leiturando.entity.User;
import br.com.leiturando.service.requests.AcceptRequestService;
import br.com.leiturando.service.requests.RemoveRequestService;
import br.com.leiturando.service.requests.RequestsService;
import br.com.leiturando.service.requests.SendRequestsService;
import com.amazonaws.services.kms.model.NotFoundException;
import com.amazonaws.services.pinpoint.model.BadRequestException;
import com.amazonaws.services.pinpoint.model.InternalServerErrorException;
import org.hibernate.procedure.ParameterStrategyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/request")
public class RequestsController {
    @Autowired
    SendRequestsService sendRequestsService;

    @Autowired
    AcceptRequestService acceptRequestService;

    @Autowired
    RequestsService requestsService;

    @Autowired
    RemoveRequestService removeRequestService;

    @GetMapping("/getRequests")
    public RequestResponse getRequests() {
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return requestsService.getRequests(user.getEmail());
    }

    @PostMapping("/send/{requestedId}")
    public ResponseEntity<String> sendRequest(@PathVariable Long requestedId) {
        try {
            User user = (User) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();
            return sendRequestsService.sendRequest(user.getEmail(), requestedId);
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

    @PostMapping("/accept/{requesterId}")
    public void acceptRequest(@PathVariable Long requesterId) {
        try {
            User user = (User) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();

            acceptRequestService.acceptRequest(user.getEmail(), requesterId);
        } catch (NotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            throw new NotFoundException(errorResponse.getMessage());
        }
    }

    @DeleteMapping("/remove/{requesterId}")
    public void removeRequest(@PathVariable Long requesterId) {
        try {
            User user = (User) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();
            removeRequestService.removeRequest(user.getEmail(), requesterId);
        } catch (NotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            throw new NotFoundException(errorResponse.getMessage());
        }
    }
}
