package com.luiz.like_service.exceptions;

public class PinNotFoundException extends RuntimeException {
  public PinNotFoundException(String message) {
    super(message);
  }
}
