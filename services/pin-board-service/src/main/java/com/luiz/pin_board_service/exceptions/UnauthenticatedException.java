package com.luiz.pin_board_service.exceptions;

public class UnauthenticatedException extends RuntimeException {
  public UnauthenticatedException(String message) {
    super(message);
  }
}

