package com.yasitha.test1.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonLoginResponse {
    String token;
    String message;
    ErrorDTO error;

    public PersonLoginResponse(String token, String message){
        this.token=token;
        this.message=message;
    }
    public PersonLoginResponse(ErrorDTO error){
        this.error=error;
    }
    public PersonLoginResponse(String message){
        this.message=message;
    }
}

