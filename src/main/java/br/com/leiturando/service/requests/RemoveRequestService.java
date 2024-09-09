package br.com.leiturando.service.requests;

import br.com.leiturando.entity.FriendRequests;
import br.com.leiturando.entity.User;
import br.com.leiturando.repository.FriendRequestRepository;
import br.com.leiturando.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RemoveRequestService {
    UserRepository userRepository;
    FriendRequestRepository friendRequestRepository;

    public ResponseEntity<String> removeRequest(String email, Long requesterId) {
        User myUser = userRepository.findByEmail(email);
        Optional<User> requester = userRepository.findById(requesterId);

        if(requester.isEmpty()) {
            return new ResponseEntity<>("Usuário não encontrado.", HttpStatus.NOT_FOUND);
        }

        Optional<FriendRequests> request = friendRequestRepository.findByRequestedAndRequester(myUser.getId(), requester.get().getId());

        if(request.isEmpty()) {
            return new ResponseEntity<>("Solicitação não encontrada.", HttpStatus.NOT_FOUND);
        }

        friendRequestRepository.delete(request.get());

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
