package br.com.leiturando.service.requests;

import br.com.leiturando.entity.FriendRequests;
import br.com.leiturando.entity.User;
import br.com.leiturando.mapper.RequestMapper;
import br.com.leiturando.repository.FriendRequestRepository;
import br.com.leiturando.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SendRequestsService {
    @Autowired
    FriendRequestRepository friendRequestRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RequestMapper sendRequestMapper;

    public ResponseEntity<String> sendRequest(String email, Long requestedId)  {
        User requester = userRepository.findByEmail(email);
        Optional<User> requested = userRepository.findById(requestedId);
        boolean isRequestExists = friendRequestRepository.findByRequestedAndRequester(requester.getId(), requestedId).isPresent();

        if(isRequestExists) {
            return new ResponseEntity<>("Solicitação já enviada.", HttpStatus.BAD_REQUEST);
        }

        if(requested.isEmpty()) {
            return new ResponseEntity<>("Usuário não encontrado.", HttpStatus.BAD_REQUEST);
        }

        if(requested.get().equals(requester)) {
            return new ResponseEntity<>("Você não pode enviar uma solicitação para si mesmo.", HttpStatus.BAD_REQUEST);
        }

        FriendRequests request = sendRequestMapper.createRequest(requester, requested.get());

        friendRequestRepository.save(request);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
