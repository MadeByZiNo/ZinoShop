package com.JH.JhOnlineJudge.exception;

import com.JH.JhOnlineJudge.category.exception.NotFoundCategoryException;
import com.JH.JhOnlineJudge.order.exception.NotFoundOrderException;
import com.JH.JhOnlineJudge.product.exception.NotFoundProductException;
import com.JH.JhOnlineJudge.product.exception.S3FileUploadException;
import com.JH.JhOnlineJudge.user.exception.AccessDeniedException;
import com.JH.JhOnlineJudge.user.exception.LoginInvalidException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class MvcExceptionHandler {

    @ExceptionHandler(NotFoundProductException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundProductException(NotFoundProductException ex) {
        return "exception/handleNotFoundProductException";
    }

    @ExceptionHandler(NotFoundCategoryException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundCategoryException(NotFoundCategoryException ex) {
        return "exception/handleNotFoundCategoryException";
    }

    @ExceptionHandler(NotFoundOrderException.class)
       @ResponseStatus(HttpStatus.NOT_FOUND)
       public String NotFoundOrderException(NotFoundOrderException ex) {
           return "exception/handleNotFoundOrderException";
       }


    @ExceptionHandler(S3FileUploadException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String s3FileUploadException(S3FileUploadException ex) {
        return "exception/S3FileUploadException";
      }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
       public String accessDeniedException(AccessDeniedException ex) {
           return "exception/AccessDeniedException";
       }


   @ExceptionHandler(LoginInvalidException.class)
   @ResponseStatus(HttpStatus.UNAUTHORIZED)
   public String loginInvalidException(LoginInvalidException ex) {
          return  "exception/LoginInvalidException";
    }

}