package com.yasitha.test1.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonLoginResponse {
    String token;
    String message;

    public PersonLoginResponse(String token, String message){
        this.token=token;
        this.message=message;
    }
    public PersonLoginResponse(String message){
        this.message=message;
    }
}

