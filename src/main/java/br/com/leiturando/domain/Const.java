package br.com.leiturando.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Const {
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_CLIENT = "ROLE_CLIENT";

    public static final Integer SCORE_TO_ADVANCE_LEVEL = 50;

    public static final Integer SCORE_WORDS = 2;
    public static final Integer SCORE_TO_CONNECT_WORDS = 1;
    public static final Integer SCORE_MATH = 3;
}
