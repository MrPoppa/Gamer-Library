package com.benji.exceptions;

/**
 *
 * @author Benjamin Bengtsson
 */
public class DataNotFoundException extends RuntimeException{

    public DataNotFoundException(String errorMessage) {
        super(errorMessage);
    }
    
}
