package com.example.plumberservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PlumberUnavailableException extends RuntimeException {
    public PlumberUnavailableException(String message) {
        super(message);
    }
}
