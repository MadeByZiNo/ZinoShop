package com.JH.JhOnlineJudge.common.exception;

import com.JH.JhOnlineJudge.domain.Image.S3FileUploadException;
import com.JH.JhOnlineJudge.domain.category.exception.NotFoundCategoryException;
import com.JH.JhOnlineJudge.domain.inquiry.exception.DuplicateInquiryReplyException;
import com.JH.JhOnlineJudge.domain.inquiry.exception.InquiryPermissionException;
import com.JH.JhOnlineJudge.domain.inquiry.exception.NotFoundInquiryException;
import com.JH.JhOnlineJudge.domain.order.exception.NotFoundOrderException;
import com.JH.JhOnlineJudge.domain.product.exception.NotFoundProductException;
import com.JH.JhOnlineJudge.domain.review.exception.DuplicateReviewException;
import com.JH.JhOnlineJudge.domain.review.exception.NotFoundReviewException;
import com.JH.JhOnlineJudge.domain.review.exception.ReviewPermissionException;
import com.JH.JhOnlineJudge.domain.user.exception.AccessDeniedException;
import com.JH.JhOnlineJudge.domain.user.exception.LoginInvalidException;
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

    @ExceptionHandler(NotFoundInquiryException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundInquiryException(NotFoundInquiryException ex) {
        return "exception/handleNotFoundInquiryException";
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

    @ExceptionHandler(NotFoundReviewException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFoundReviewException(NotFoundReviewException ex) {
        return "exception/handleNotFoundReviewException";
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

    @ExceptionHandler(ReviewPermissionException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String reviewPermissionException(ReviewPermissionException ex){
        return "exception/ReviewPermissionException";
    }

    @ExceptionHandler(InquiryPermissionException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String inquiryPermissionException(InquiryPermissionException ex){
        return "exception/InquiryPermissionException";
    }


    @ExceptionHandler(DuplicateReviewException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String duplicateReviewException(DuplicateReviewException ex){
        return "exception/DuplicateReviewException";
    }

    @ExceptionHandler(DuplicateInquiryReplyException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String duplicateInquiryReplyException(DuplicateInquiryReplyException ex){
        return "exception/DuplicateInquiryReplyException";
    }



}