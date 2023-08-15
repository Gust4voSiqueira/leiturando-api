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
class AcceptRequestServiceTest {
    @InjectMocks
    AcceptRequestService acceptRequestService;

    @Mock
    UserRepository userRepository;

    @Mock
    FriendRequestRepository friendRequestRepository;

    User requester;
    User user3;

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
        user3 = User
                .builder()
                .id(3L)
                .name("Matias")
                .email("matias@gmail.com")
                .password(PASSWORD_DEFAULT)
                .image("Flash")
                .friendships(List.of())
                .build();
    }

    @Test
    void failedToacceptRequestThatIsNotFound() {
        when(userRepository.findByEmail(user3.getEmail())).thenReturn(user3);
        when(userRepository.findById(requester.getId())).thenReturn(Optional.ofNullable(requester));
        when(friendRequestRepository.findByRequestedAndRequester(user3.getId(), requester.getId())).thenReturn(null);

        String emailMyUser = user3.getEmail();
        Long idRequester = requester.getId();

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class,
                () -> acceptRequestService.acceptRequest(emailMyUser, idRequester));

        Assertions.assertNotNull(exception.getMessage());
    }
}
