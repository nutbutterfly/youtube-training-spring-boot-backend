package com.iamnbty.training.backend.exception;

public class ChatException extends BaseException {

    public ChatException(String code) {
        super("chat." + code);
    }

    public static ChatException accessDenied() {
        return new ChatException("access.denied");
    }

}