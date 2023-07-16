package br.com.leiturando;

import br.com.leiturando.entity.UserTest;
import br.com.leiturando.service.UserRepositoryUserDetails;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@SuppressWarnings("squid:S5786")
public class BaseAuthTest {
    protected UserRepositoryUserDetails userAuth;

    protected Authentication authentication;

    public BaseAuthTest(){
        userAuth = new UserRepositoryUserDetails(UserTest.builderUser());
        authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when((UserRepositoryUserDetails) authentication.getPrincipal())
                .thenReturn(userAuth);
    }

    @Test
    void baseTest(){
        Assertions.assertNotNull(userAuth);
    }
}
