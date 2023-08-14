package br.com.leiturando.service.requests;

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

    @BeforeEach
    public void init() {
        requester = User
                .builder()
                .id(2L)
                .name("Brena")
                .email("brena@gmail.com")
                .password(PASSWORD_DEFAULT)
                .imageUrl("Batman")
                .friendships(List.of())
                .build();
        myUser = User
                .builder()
                .id(3L)
                .name("Matias")
                .email("matias@gmail.com")
                .password(PASSWORD_DEFAULT)
                .imageUrl("Flash")
                .friendships(List.of())
                .build();
    }
    @Test
    void failedToRemoveRequestThatIsMine() {
        when(userRepository.findByEmail(myUser.getEmail())).thenReturn(myUser);
        when(userRepository.findById(requester.getId())).thenReturn(Optional.ofNullable(requester));
        when(friendRequestRepository.findByRequestedAndRequester(myUser.getId(), requester.getId())).thenReturn(null);

        String emailMyUser = myUser.getEmail();
        Long idRequester = requester.getId();

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class,
                () -> removeRequestService.removeRequest(emailMyUser, idRequester));

        Assertions.assertNotNull(exception.getMessage());
    }
}
