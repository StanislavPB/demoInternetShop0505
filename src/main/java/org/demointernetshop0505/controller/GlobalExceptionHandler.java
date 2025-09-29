package org.demointernetshop0505.controller;

import jakarta.validation.ConstraintViolationException;
import org.demointernetshop0505.dto.ApiError;
import org.demointernetshop0505.service.exception.AlreadyExistException;
import org.demointernetshop0505.service.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handlerDateTimeParseException(DateTimeParseException e){
        return new ResponseEntity<>( e.getMessage() , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handlerNullPointerException(NullPointerException e){
        return new ResponseEntity<>( e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handlerNotFoundException(NotFoundException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<String> handlerAlreadyExistException(AlreadyExistException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handlerConstraintViolationException(ConstraintViolationException e){
        StringBuilder responseMessage = new StringBuilder();

        e.getConstraintViolations().forEach(
                constraintViolation -> {
                    String currentField = constraintViolation.getPropertyPath().toString();
                    String currentMessage = constraintViolation.getMessage();
                    responseMessage.append("В поле: " + currentField + " : " + currentMessage);
                    responseMessage.append("\n");
                }
        );
        return new ResponseEntity<>(responseMessage.toString(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException e){
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body(Map.of("error","Такой пользователь на зарегистрирован"));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException e){
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body(Map.of("error","Неверный логин или пароль"));
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handlerMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e){
       ApiError error = ApiError.builder()
               .error("Invalid parameter")
               .message("Failed to convert parameter value")
               .parameter(e.getName())
               .rejectedValue(e.getValue())
               .timestamp(LocalDateTime.now())
               .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
