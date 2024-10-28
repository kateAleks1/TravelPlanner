package org.example.exception;

import org.springframework.http.HttpStatus;

public class GeneralException extends RuntimeException{
    private HttpStatus httpStatus;

    public GeneralException(String message) {
      super(message);
    }
    public GeneralException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
