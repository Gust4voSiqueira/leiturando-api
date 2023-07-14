package br.com.leiturando.service;


import br.com.leiturando.entity.Role;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import br.com.leiturando.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class MyUserDetailsServiceTest {

    @InjectMocks
    MyUserDetailsService myUserDetailsService;

    @Mock
    UserRepository userRepository;

    UserDetails userDetails;

    String email;

    User user;
    Role role;
    List<Role> roles;

    @Before
    public void init() {
        email = "gustavo@gmail.com";
        role = Role.builder().id(1L).name("a").build();
        roles = new ArrayList<>(Collections.singletonList(role));
        user = UserTest.builderUser();
        userDetails = new UserRepositoryUserDetails(user);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void setMyUserDetailsServiceException() {
        myUserDetailsService.loadUserByUsername("joão@gmail.com");
    }

    @Test
    public void setMyUserDetailsService() {
        when(userRepository.findByEmail(email))
                .thenReturn(user);

        var result = myUserDetailsService.loadUserByUsername(email);

        assertEquals(result.getUsername(), user.getEmail());
        assertEquals(result.getAuthorities(), user.getRoles());
        assertEquals(result.getPassword(), user.getPassword());
        assertNotNull(role.getAuthority());
    }

}
