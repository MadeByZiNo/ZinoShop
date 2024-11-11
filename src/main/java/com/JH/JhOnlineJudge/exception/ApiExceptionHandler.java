package com.JH.JhOnlineJudge.exception;

import com.JH.JhOnlineJudge.cart.exception.InvalidQuantityException;
import com.JH.JhOnlineJudge.category.exception.NotFoundCategoryException;
import com.JH.JhOnlineJudge.order.exception.InsufficientRemainException;
import com.JH.JhOnlineJudge.order.exception.NotFoundOrderException;
import com.JH.JhOnlineJudge.product.exception.NotFoundProductException;
import com.JH.JhOnlineJudge.product.exception.S3FileUploadException;
import com.JH.JhOnlineJudge.user.exception.AccessDeniedException;
import com.JH.JhOnlineJudge.user.exception.LoginInvalidException;
import jakarta.servlet.http.HttpServletRequest;
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

}