package com.luiz.backend.exception;

public class PinBoardNotFoundException extends RuntimeException {
  public PinBoardNotFoundException(String message) {
    super(message);
  }
}
