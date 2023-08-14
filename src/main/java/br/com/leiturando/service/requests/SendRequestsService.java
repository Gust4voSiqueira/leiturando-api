package br.com.leiturando.service.requests;

import br.com.leiturando.controller.response.UserResponse;
import br.com.leiturando.entity.FriendRequests;
import br.com.leiturando.entity.User;
import br.com.leiturando.mapper.RequestMapper;
import br.com.leiturando.mapper.UserMapper;
import br.com.leiturando.repository.FriendRequestRepository;
import br.com.leiturando.repository.UserRepository;
import com.amazonaws.services.kms.model.NotFoundException;
import org.hibernate.procedure.ParameterStrategyException;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    UserMapper userMapper;

    public UserResponse sendRequest(String email, Long requestedId)  {
        User requester = userRepository.findByEmail(email);
        Optional<User> requested = userRepository.findById(requestedId);

        if(requested.isEmpty()) {
            throw new NotFoundException("Usuário não encontrado.");
        }

        if(requested.get() == requester) {
            throw new ParameterStrategyException("Você não pode enviar uma solicitação para si mesmo.");
        }

        FriendRequests request = sendRequestMapper.createRequest(requester, requested.get());

        friendRequestRepository.save(request);

        return userMapper.userToResponse(requested.get());
    }
}
