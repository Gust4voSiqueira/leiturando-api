package br.com.leiturando.entity;


import br.com.leiturando.domain.Const;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static br.com.leiturando.Consts.PASSWORD_DEFAULT;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
public class UserTest {

    public static User builderUser() {
        var role = new Role(Const.ROLE_ADMIN);;
        return User.builder()
                .id(1L)
                .name("gustavo")
                .email("gustavo@gmail.com")
                .password(PASSWORD_DEFAULT)
                .imageUrl("Batman")
                .roles(List.of(role))
                .level(1)
                .breakthrough(0)
                .friendships(List.of())
                .build();
    }

    @Test
    public void buildUserCorrectly(){
        var result = builderUser();
        assertNotNull(result.getId());
        assertNotNull(result.getName());
        assertNotNull(result.getEmail());
        assertNotNull(result.getPassword());
        assertNotNull(result.getImageUrl());
        assertNotNull(result.getRoles());
        assertNotNull(result.getLevel());
        assertNotNull(result.getBreakthrough());
        assertNotNull(result.getFriendships());
    }
}
