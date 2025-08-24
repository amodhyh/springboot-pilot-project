package com.yasitha.test1.DTO;

import lombok.Getter;

import java.util.Set;

@Getter
public class ErrorDTO {
    private final Set<String> messages;

    public  ErrorDTO(Set<String> message) {
        this.messages = message;

    }


}