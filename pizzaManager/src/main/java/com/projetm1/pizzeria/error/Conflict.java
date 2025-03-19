package com.projetm1.pizzeria.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class Conflict extends RuntimeException {
    public Conflict(String message) {
        super(message);
    }
}
