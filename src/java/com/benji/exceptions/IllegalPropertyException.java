package com.benji.exceptions;

/**
 *
 * @author Benjamin
 */
public class IllegalPropertyException extends IllegalArgumentException{

    public IllegalPropertyException(String errorMessage) {
        super(errorMessage);
    }
    
}
