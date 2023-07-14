package br.com.leiturando.exception;

public class UserExistsException extends Exception {
    public UserExistsException(String mensagem) {
        super(mensagem);
    }
}
