package br.com.leiturando.service.requests;

import br.com.leiturando.controller.response.RequestResponse;
import br.com.leiturando.entity.FriendRequests;
import br.com.leiturando.entity.User;
import br.com.leiturando.repository.FriendRequestRepository;
import br.com.leiturando.repository.UserRepository;
import com.amazonaws.services.kms.model.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RemoveRequestService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    FriendRequestRepository friendRequestRepository;

    @Autowired
    RequestsService requestsService;

    public RequestResponse removeRequest(String email, Long requesterId) {
        User myUser = userRepository.findByEmail(email);
        Optional<User> requester = userRepository.findById(requesterId);

        if(requester.isEmpty()) {
            throw new NotFoundException("Usuário não encontrado.");
        }

        FriendRequests request = friendRequestRepository.findByRequestedAndRequester(myUser, requester.get());

        if(request == null) {
            throw new NotFoundException("Solicitação não encontrada.");
        }

        friendRequestRepository.delete(request);

        return requestsService.getRequests(myUser.getEmail());
    }
}
