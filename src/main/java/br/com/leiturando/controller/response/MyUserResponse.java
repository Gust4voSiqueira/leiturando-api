package br.com.leiturando.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MyUserResponse {
    private String imageUrl;
    private String name;
    private Integer level;
    private Integer breakthrough;
}
