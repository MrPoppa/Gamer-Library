package com.benji.exceptions;

/**
 *
 * @author Benjamin
 */
public class DataNotFoundException extends RuntimeException{

    public DataNotFoundException(String errorMessage) {
        super(errorMessage);
    }
    
}
