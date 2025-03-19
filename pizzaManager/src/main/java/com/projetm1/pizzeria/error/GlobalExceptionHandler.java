package com.projetm1.pizzeria.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, String>> buildResponseEntity(HttpStatus status, String message) {
        Map<String, String> body = new HashMap<>();
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(BadRequest.class)
    public ResponseEntity<Map<String, String>> badRequest(BadRequest ex) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(Conflict.class)
    public ResponseEntity<Map<String, String>> conflict(Conflict ex) {
        return buildResponseEntity(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(NotFound.class)
    public ResponseEntity<Map<String, String>> notFound(NotFound ex) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(UnprocessableEntity.class)
    public ResponseEntity<Map<String, String>> unprocessableEntity(UnprocessableEntity ex) {
        return buildResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }
}
