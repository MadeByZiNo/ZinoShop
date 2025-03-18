package com.JH.JhOnlineJudge.common.exception;

import com.JH.JhOnlineJudge.cart.exception.InvalidQuantityException;
import com.JH.JhOnlineJudge.order.exception.InsufficientRemainException;
import com.JH.JhOnlineJudge.order.exception.InvalidOrderException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(InvalidQuantityException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> invalidQuantityException(InvalidQuantityException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
     }

    @ExceptionHandler(InsufficientRemainException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
      public ResponseEntity<String> InsufficientRemainException(InsufficientRemainException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
      }

    @ExceptionHandler(InvalidOrderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> InvalidOrderException(InvalidOrderException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}