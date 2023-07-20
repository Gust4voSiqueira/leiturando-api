package br.com.leiturando.entity;

import br.com.leiturando.domain.Const;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static br.com.leiturando.Consts.PASSWORD_DEFAULT;

@ExtendWith(MockitoExtension.class)
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
    void buildUserCorrectly(){
        var result = builderUser();
        Assertions.assertNotNull(result.getId());
        Assertions.assertNotNull(result.getName());
        Assertions.assertNotNull(result.getEmail());
        Assertions.assertNotNull(result.getPassword());
        Assertions.assertNotNull(result.getImageUrl());
        Assertions.assertNotNull(result.getRoles());
        Assertions.assertNotNull(result.getLevel());
        Assertions.assertNotNull(result.getBreakthrough());
        Assertions.assertNotNull(result.getFriendships());
    }
}
