package com.luiz.backend.exception;

public class BoardNotFoundException extends RuntimeException {
  public BoardNotFoundException(String message) {
    super(message);
  }
}
