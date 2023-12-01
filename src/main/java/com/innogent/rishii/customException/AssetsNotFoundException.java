package com.innogent.rishii.customException;

public class AssetsNotFoundException extends RuntimeException{
    public AssetsNotFoundException(String message) {
        super(message);
    }
}
