package com.yasitha.test1.ExceptionHandling;

public class InvalidCredentials extends RuntimeException{
    public InvalidCredentials(String message){
        super(message);
    }
}
