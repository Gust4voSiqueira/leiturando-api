package br.com.leiturando.mapper;

import br.com.leiturando.controller.response.MyUserResponse;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class MyUserMapperTest {
    @InjectMocks
    MyUserMapper myUserMapper;
    User user;

    @Test
    void setMyUserToResponse() {
        user = UserTest.builderUser();

        MyUserResponse result = myUserMapper.myUserDtoToResponse(user, user.getImageUrl());
        MyUserResponse expected = MyUserResponse
                .builder()
                    .imageUrl(user.getImageUrl())
                    .name(user.getName())
                    .level(user.getLevel())
                    .breakthrough(user.getBreakthrough())
                .build();


        Assertions.assertEquals(expected.getName(), result.getName());
        Assertions.assertEquals(expected.getImageUrl(), result.getImageUrl());
        Assertions.assertEquals(expected.getLevel(), result.getLevel());
        Assertions.assertEquals(expected.getBreakthrough(), result.getBreakthrough());
    }

}
