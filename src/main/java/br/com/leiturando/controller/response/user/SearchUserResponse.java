package br.com.leiturando.controller.response.user;

import br.com.leiturando.domain.TypesCard;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SearchUserResponse {
    private Long id;
    private String name;
    private String image;
    private Integer mutualFriends;
    private TypesCard typeCard;
}
