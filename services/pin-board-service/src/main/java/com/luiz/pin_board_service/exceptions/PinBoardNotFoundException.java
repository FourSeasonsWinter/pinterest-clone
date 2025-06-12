package com.luiz.pin_board_service.exceptions;

public class PinBoardNotFoundException extends RuntimeException {
  public PinBoardNotFoundException(String message) {
    super(message);
  }
}
