package br.com.leiturando.mapper;

import br.com.leiturando.controller.response.MyUserResponse;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class MyUserMapperTest {
    @InjectMocks
    MyUserMapper myUserMapper;
    User user;

    @Test
    void setMyUserToResponse() {
        user = UserTest.builderUser();

        MyUserResponse result = myUserMapper.myUserDtoToResponse(user, List.of(), List.of(), List.of());
        MyUserResponse expected = MyUserResponse
                .builder()
                    .imageUrl(user.getImageUrl())
                    .name(user.getName())
                    .level(user.getLevel())
                    .breakthrough(user.getBreakthrough())
                    .usersRecomended(List.of())
                    .requests(List.of())
                    .friends(List.of())
                .build();


        Assertions.assertEquals(expected.getName(), result.getName());
        Assertions.assertEquals(expected.getImageUrl(), result.getImageUrl());
        Assertions.assertEquals(expected.getLevel(), result.getLevel());
        Assertions.assertEquals(expected.getBreakthrough(), result.getBreakthrough());
        Assertions.assertEquals(expected.getUsersRecomended(), result.getUsersRecomended());
        Assertions.assertEquals(expected.getFriends(), result.getFriends());
        Assertions.assertEquals(expected.getRequests(), result.getRequests());
    }

}
