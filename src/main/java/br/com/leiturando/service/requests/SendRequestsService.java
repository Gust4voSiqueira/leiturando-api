package br.com.leiturando.service.requests;

import br.com.leiturando.controller.response.ListRequestsResponse;
import br.com.leiturando.controller.response.SendRequestResponse;
import br.com.leiturando.entity.FriendRequests;
import br.com.leiturando.entity.User;
import br.com.leiturando.mapper.RequestMapper;
import br.com.leiturando.repository.FriendRequestRepository;
import br.com.leiturando.repository.UserRepository;
import br.com.leiturando.service.FileService;
import com.amazonaws.services.kms.model.NotFoundException;
import org.hibernate.procedure.ParameterStrategyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static br.com.leiturando.domain.Const.CHARACTERS_LIST;

@Service
public class SendRequestsService {
    @Autowired
    FriendRequestRepository friendRequestRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RequestMapper sendRequestMapper;

    @Autowired
    FileService fileService;

    public SendRequestResponse sendRequest(String email, Long requestedId)  {
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

        return sendRequestMapper.requestToResponse(request);
    }

    public List<ListRequestsResponse> searchRequestsReceived(User user) {
        List<FriendRequests> requests = friendRequestRepository.findAllByRequestedId(user.getId());

        if(requests.isEmpty()) {
            return List.of();
        }

        List<User> requestsReceivedUsers = requests.stream().map(request -> userRepository.findById(request.getRequester().getId()).get()).collect(Collectors.toList());

        return requestsReceivedUsers.stream().map(userLocal -> {
            String image;
            if(CHARACTERS_LIST.contains(userLocal.getImageUrl())) {
                image = fileService.downloadFile(userLocal.getImageUrl() + ".png");
            } else {
                image = fileService.downloadFile(userLocal.getImageUrl());
            }

            return sendRequestMapper.myUserResponse(userLocal, image, searchMutualFriends(user, userLocal));
        }).collect(Collectors.toList());
    }

    public List<ListRequestsResponse> searchRequestsSend(User user) {
        List<FriendRequests> requests = friendRequestRepository.findAllByRequesterId(user.getId());

        if(requests.isEmpty()) {
            return List.of();
        }

        List<User> requestsSendUsers = requests
                .stream()
                .map(request -> userRepository.findById(request.getRequested().getId()).get())
                .collect(Collectors.toList());

        return requestsSendUsers
                .stream()
                .map(userLocal -> {
                    String image;
                    if(CHARACTERS_LIST.contains(userLocal.getImageUrl())) {
                        image = fileService.downloadFile(userLocal.getImageUrl() + ".png");
                    } else {
                        image = fileService.downloadFile(userLocal.getImageUrl());
                    }

                    return sendRequestMapper.myUserResponse(userLocal, image, searchMutualFriends(user, userLocal));
                })
                .collect(Collectors.toList());
    }

    public Integer searchMutualFriends(User myUser, User userCompared) {
        List<User> myListFriends = myUser.getFriendships().stream()
                .flatMap(friendship -> Stream.of(friendship.getUser(), friendship.getFriend()))
                .filter(user -> user != myUser)
                .distinct()
                .collect(Collectors.toList());

        List<User> listFriendsMyFriendship = userCompared.getFriendships().stream()
                .flatMap(friendship -> Stream.of(friendship.getUser(), friendship.getFriend()))
                .filter(user -> user != myUser)
                .distinct()
                .collect(Collectors.toList());

        return (int) myListFriends.stream().filter(listFriendsMyFriendship::contains).count();
    }
}
