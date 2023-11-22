package br.com.leiturando.controller;

import br.com.leiturando.BaseAuthTest;
import br.com.leiturando.controller.request.user.UpdateUserRequest;
import br.com.leiturando.controller.request.user.UpdateUserRequestTest;
import br.com.leiturando.controller.response.user.MyUserResponse;
import br.com.leiturando.controller.response.user.MyUserResponseTest;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import br.com.leiturando.service.MyUserService;
import com.amazonaws.services.pinpoint.model.BadRequestException;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityControllerTest extends BaseAuthTest {
    @InjectMocks
    SecurityController securityController;

    @Mock
    MyUserService myUserService;

    User user;
    UpdateUserRequest updateUserRequest;
    MyUserResponse myUserResponse;

    @BeforeEach
    public void init() {
        updateUserRequest = UpdateUserRequestTest.builderUpdateUserRequest();
        user = UserTest.builderUser();
        myUserResponse = MyUserResponseTest.builderMyUserResponse();
    }

    @Test
    void failedToEditProfileNewPasswordDifferentConfirmPassword() {
        updateUserRequest.setConfirmNewPassword("12345");
        when(myUserService.editProfile(user.getEmail(), updateUserRequest)).thenThrow(new BadRequestException("As senhas não conferem."));
        Assert.assertThrows(BadRequestException.class, () -> securityController.editProfile(updateUserRequest));
    }

    @Test
    void updateUserCorrectly() {
        when(myUserService.editProfile(user.getEmail(), updateUserRequest)).thenReturn(ResponseEntity.ok().body(myUserResponse));

        var result = securityController.editProfile(updateUserRequest);

        Assertions.assertEquals(ResponseEntity.ok().body(myUserResponse), result);
    }
}
