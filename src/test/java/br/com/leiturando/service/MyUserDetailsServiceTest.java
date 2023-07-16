package br.com.leiturando.service;

import br.com.leiturando.entity.Role;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import br.com.leiturando.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class MyUserDetailsServiceTest {

    @InjectMocks
    MyUserDetailsService myUserDetailsService;

    @Mock
    UserRepository userRepository;

    UserDetails userDetails;

    String email;

    User user;
    Role role;
    List<Role> roles;

    @BeforeEach
    public void init() {
        email = "gustavo@gmail.com";
        role = Role.builder().id(1L).name("a").build();
        roles = new ArrayList<>(Collections.singletonList(role));
        user = UserTest.builderUser();
        userDetails = new UserRepositoryUserDetails(user);
    }

    @Test
    void setMyUserDetailsService() {
        Mockito.when(userRepository.findByEmail(email))
                .thenReturn(user);

        var result = myUserDetailsService.loadUserByUsername(email);

        Assertions.assertEquals(result.getUsername(), user.getEmail());
        Assertions.assertEquals(result.getAuthorities(), user.getRoles());
        Assertions.assertEquals(result.getPassword(), user.getPassword());
        Assertions.assertNotNull(role.getAuthority());
    }

    @Test
    void setMyUserDetailsServiceException() {
        UsernameNotFoundException exception = Assertions.assertThrows(UsernameNotFoundException.class,
                () -> myUserDetailsService.loadUserByUsername("joão@gmail.com"));

        Assertions.assertEquals("Usuário não existe!", exception.getLocalizedMessage());
    }
}
