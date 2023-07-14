package br.com.leiturando.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class SecurityControllerTest {
    @InjectMocks
    SecurityController securityController;

    @Test(expected = NullPointerException.class)
    public void failedToAccessMyUserWithoutToken() {
        securityController.user();
    }
}
