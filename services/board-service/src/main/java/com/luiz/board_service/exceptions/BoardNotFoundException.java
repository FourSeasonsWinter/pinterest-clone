package com.luiz.board_service.exceptions;

public class BoardNotFoundException extends RuntimeException {
  public BoardNotFoundException(String message) {
    super(message);
  }
}
