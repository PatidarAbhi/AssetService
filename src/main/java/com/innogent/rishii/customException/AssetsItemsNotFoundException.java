package com.innogent.rishii.customException;

public class AssetsItemsNotFoundException extends RuntimeException{
    public AssetsItemsNotFoundException(String message) {
        super(message);
    }
}
