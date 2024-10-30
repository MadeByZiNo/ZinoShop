package com.JH.JhOnlineJudge.category.exception;

public class NotFoundCategoryException extends RuntimeException{
    public NotFoundCategoryException(Long id) {
        super("Product with ID " + id + " not found");
    }
}
