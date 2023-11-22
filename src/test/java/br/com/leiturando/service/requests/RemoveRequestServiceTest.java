package br.com.leiturando.service.requests;

import br.com.leiturando.entity.FriendRequests;
import br.com.leiturando.entity.User;
import br.com.leiturando.repository.FriendRequestRepository;
import br.com.leiturando.repository.UserRepository;
import com.amazonaws.services.kms.model.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static br.com.leiturando.Consts.PASSWORD_DEFAULT;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RemoveRequestServiceTest {
    @InjectMocks
    RemoveRequestService removeRequestService;

    @Mock
    UserRepository userRepository;

    @Mock
    FriendRequestRepository friendRequestRepository;

    User requester;
    User myUser;
    FriendRequests friendRequest;

    @BeforeEach
    public void init() {
        requester = User
                .builder()
                .id(2L)
                .name("Brena")
                .email("brena@gmail.com")
                .password(PASSWORD_DEFAULT)
                .image("Batman")
                .friendships(List.of())
                .build();
        myUser = User
                .builder()
                .id(3L)
                .name("Matias")
                .email("matias@gmail.com")
                .password(PASSWORD_DEFAULT)
                .image("Flash")
                .friendships(List.of())
                .build();

        friendRequest = FriendRequests
                .builder()
                .id(1L)
                .requested(myUser)
                .requester(requester)
                .build();
    }
    @Test
    void removeRequestCorrectly() {
        when(userRepository.findByEmail(myUser.getEmail())).thenReturn(myUser);
        when(userRepository.findById(requester.getId())).thenReturn(Optional.ofNullable(requester));
        when(friendRequestRepository.findByRequestedAndRequester(myUser.getId(), requester.getId())).thenReturn(Optional.of(friendRequest));

        var result = removeRequestService.removeRequest(myUser.getEmail(), requester.getId());
        var expected =  new ResponseEntity<>(HttpStatus.ACCEPTED);

        Assertions.assertEquals(result, expected);
    }

    @Test
    void failedToRemoveRequestUserNotFound() {
        when(userRepository.findByEmail(myUser.getEmail())).thenReturn(myUser);
        when(userRepository.findById(3L)).thenReturn(Optional.empty());

        var result = removeRequestService.removeRequest(myUser.getEmail(), 3L);
        var expected =  new ResponseEntity<>("Usuário não encontrado.", HttpStatus.NOT_FOUND);

        Assertions.assertEquals(expected, result);
    }

    @Test
    void failedToRemoveRequestNotFound() {
        when(userRepository.findByEmail(myUser.getEmail())).thenReturn(myUser);
        when(userRepository.findById(requester.getId())).thenReturn(Optional.ofNullable(requester));
        when(friendRequestRepository.findByRequestedAndRequester(myUser.getId(), requester.getId())).thenReturn(Optional.empty());

        var result = removeRequestService.removeRequest(myUser.getEmail(), requester.getId());
        var expected =  new ResponseEntity<>("Solicitação não encontrada.", HttpStatus.NOT_FOUND);

        Assertions.assertEquals(result, expected);
    }
}
