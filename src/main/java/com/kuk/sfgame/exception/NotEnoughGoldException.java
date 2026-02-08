package com.kuk.sfgame.exception;

public class NotEnoughGoldException extends RuntimeException {
    public NotEnoughGoldException(String message) {
        super(message);
    }
}
