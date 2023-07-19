package br.com.leiturando.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Const {
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_CLIENT = "ROLE_CLIENT";

    public static final List<String> CHARACTERS_LIST = List.of("batman", "robin", "superman", "wonder", "thor", "spiderman", "joker", "harry", "wolverine");
}
