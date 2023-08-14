package br.com.leiturando.service.requests;

import br.com.leiturando.controller.response.UserResponse;
import br.com.leiturando.entity.FriendRequests;
import br.com.leiturando.entity.User;
import br.com.leiturando.mapper.UserMapper;
import br.com.leiturando.repository.FriendRequestRepository;
import br.com.leiturando.repository.UserRepository;
import com.amazonaws.services.kms.model.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class RemoveRequestService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    FriendRequestRepository friendRequestRepository;

    @Autowired
    UserMapper userMapper;

    public UserResponse removeRequest(String email, Long requesterId) {
        User myUser = userRepository.findByEmail(email);
        Optional<User> requester = userRepository.findById(requesterId);

        if(requester.isEmpty()) {
            throw new NotFoundException("Usuário não encontrado.");
        }

        FriendRequests request = friendRequestRepository.findByRequestedAndRequester(myUser.getId(), requester.get().getId());

        if(request == null) {
            throw new NotFoundException("Solicitação não encontrada.");
        }

        friendRequestRepository.delete(request);

        if(Objects.equals(request.getRequested().getId(), myUser.getId())) {
            return userMapper.userToResponse(request.getRequester());
        }

        return userMapper.userToResponse(request.getRequested());
    }
}
