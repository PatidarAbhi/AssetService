package com.innogent.rishii.customException;

public class CountNotMatchException extends RuntimeException{
    public CountNotMatchException(String message) {
        super(message);
    }
}
