package br.com.leiturando.service.requests;

import br.com.leiturando.entity.FriendRequests;
import br.com.leiturando.entity.Friendship;
import br.com.leiturando.entity.User;
import br.com.leiturando.mapper.FriendshipMapper;
import br.com.leiturando.repository.FriendRequestRepository;
import br.com.leiturando.repository.FriendshipRepository;
import br.com.leiturando.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AcceptRequestService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    FriendRequestRepository friendRequestRepository;

    @Autowired
    FriendshipMapper friendshipMapper;

    @Autowired
    FriendshipRepository friendshipRepository;

    public ResponseEntity<String> acceptRequest(String email, Long requesterId) {
        User myUser = userRepository.findByEmail(email);
        Optional<User> requester = userRepository.findById(requesterId);

        if(requester.isEmpty()) {
            return new ResponseEntity<>("Usuário não encontrado.", HttpStatus.NOT_FOUND);
        }

        Optional<FriendRequests> request = friendRequestRepository.findByRequestedAndRequester(myUser.getId(), requester.get().getId());

        if(request.isEmpty()) {
            return new ResponseEntity<>("Solicitação não encontrada.", HttpStatus.NOT_FOUND);
        }

        Friendship friendship = friendshipMapper.requestToFriendship(request.get());

        friendshipRepository.save(friendship);
        friendRequestRepository.delete(request.get());

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
