package com.benji.exceptions;

/**
 *
 * @author Benjamin Bengtsson
 */
public class IllegalPropertyException extends IllegalArgumentException{

    public IllegalPropertyException(String errorMessage) {
        super(errorMessage);
    }
    
}
