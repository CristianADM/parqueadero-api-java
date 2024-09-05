package com.parqueadero.app.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.parqueadero.app.dtos.responses.ErrorDetailResponse;
import com.parqueadero.app.dtos.responses.ErrorResponse;
import com.parqueadero.app.exceptions.BadRequestException;
import com.parqueadero.app.exceptions.NotFoundException;
import com.parqueadero.app.exceptions.UnauthorizedException;

@RestControllerAdvice
public class HandlerExceptionController {

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException e) {
        return new ResponseEntity<>(e.getErrorResponse(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e) {
        return new ResponseEntity<>(e.getErrorResponse(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        return new ResponseEntity<>(e.getErrorResponse(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        
        List<ErrorDetailResponse> errors = new ArrayList<>();

        e.getBindingResult().getFieldErrors().forEach(er -> {
            errors.add(ErrorDetailResponse.builder()
                .error(er.getField())
                .message(er.getDefaultMessage())
                .build());
        });

        ErrorResponse error = new ErrorResponse();
        error.setErrors(errors);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
