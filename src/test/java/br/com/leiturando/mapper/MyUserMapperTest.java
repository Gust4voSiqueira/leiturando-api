package br.com.leiturando.mapper;

import br.com.leiturando.controller.response.user.MyUserResponse;
import br.com.leiturando.controller.response.user.MyUserResponseTest;
import br.com.leiturando.controller.response.user.RankingResponse;
import br.com.leiturando.controller.response.user.RankingResponseTest;
import br.com.leiturando.entity.User;
import br.com.leiturando.entity.UserTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;


@ExtendWith(MockitoExtension.class)
class MyUserMapperTest {
    @InjectMocks
    MyUserMapper myUserMapper;
    User user;

    @Test
    void setMyUserToResponse() {
        user = UserTest.builderUser();

        MyUserResponse result = myUserMapper.myUserDtoToResponse(user);
        MyUserResponse expected = MyUserResponseTest.builderMyUserResponse();

        Assertions.assertEquals(expected.getName(), result.getName());
        Assertions.assertEquals(expected.getImage(), result.getImage());
        Assertions.assertEquals(expected.getLevel(), result.getLevel());
        Assertions.assertEquals(expected.getBreakthrough(), result.getBreakthrough());
    }

    @Test
    void setUserToRankingResponse() {
        RankingResponse rankingResponse = RankingResponseTest.builderRankingResponse();

        List<List<Integer>> data = rankingResponse.getDatasets().stream().map(RankingResponse.Datasets::getData).collect(Collectors.toList());

        RankingResponse result = myUserMapper.userToRankingResponse(rankingResponse.getLabels(), data.get(0));

        Assertions.assertEquals(rankingResponse.getLabels(), result.getLabels());
        Assertions.assertEquals(rankingResponse.getDatasets(), result.getDatasets());
    }
}
