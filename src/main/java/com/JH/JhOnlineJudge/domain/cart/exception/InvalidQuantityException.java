package com.JH.JhOnlineJudge.domain.cart.exception;

public class InvalidQuantityException extends RuntimeException{
    public InvalidQuantityException() {
              super("수량은 1에서 99 사이여야 합니다.");
          }
}
