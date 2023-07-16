package br.com.leiturando.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SecurityControllerTest {
    @InjectMocks
    SecurityController securityController;

    @Test
    void failedToAccessMyUserWithoutToken() {
        NullPointerException exception = Assertions.assertThrows(NullPointerException.class,
                () -> securityController.user());

        Assertions.assertEquals("Você precisa de um token para acessar.", exception.getMessage());
    }
}
