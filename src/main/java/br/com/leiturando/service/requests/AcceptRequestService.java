package br.com.leiturando.service.requests;

import br.com.leiturando.controller.response.RequestResponse;
import br.com.leiturando.entity.FriendRequests;
import br.com.leiturando.entity.Friendship;
import br.com.leiturando.entity.User;
import br.com.leiturando.mapper.FriendshipMapper;
import br.com.leiturando.repository.FriendRequestRepository;
import br.com.leiturando.repository.FriendshipRepository;
import br.com.leiturando.repository.UserRepository;
import com.amazonaws.services.kms.model.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    RequestsService requestsService;

    public RequestResponse acceptRequest(String email, Long requesterId) {
        User myUser = userRepository.findByEmail(email);
        Optional<User> requester = userRepository.findById(requesterId);

        if(requester.isEmpty()) {
            throw new NotFoundException("Usuário não encontrado.");
        }

        FriendRequests request = friendRequestRepository.findByRequestedAndRequester(myUser, requester.get());

        if(request == null) {
            throw new NotFoundException("Solicitação não encontrada.");
        }

        Friendship friendship = friendshipMapper.requestToFriendship(request);

        friendshipRepository.save(friendship);
        friendRequestRepository.delete(request);

        return requestsService.getRequests(myUser.getEmail());
    }
}
