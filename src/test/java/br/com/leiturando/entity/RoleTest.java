package br.com.leiturando.entity;

import br.com.leiturando.domain.Const;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RoleTest {
    public static Role builderRole() {
        return Role.builder()
                .id(1L)
                .name(Const.ROLE_ADMIN)
                .build();
    }

    @Test
    void buildRoleCorrectly(){
        var result = builderRole();

        Assertions.assertNotNull(result.getId());
        Assertions.assertNotNull(result.getName());
    }
}
