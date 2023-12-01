package com.innogent.rishii.customException;

public class AssignedAssetNotFoundException extends RuntimeException{
    public AssignedAssetNotFoundException(String message) {
        super(message);
    }
}
