package com.cvprofile.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
  private final ErrorCode errorCode;
  private final String detailedMessage;

  public ApiException(ErrorCode errorCode) {
    this(errorCode, errorCode.getMessage(), null);
  }

  public ApiException(ErrorCode errorCode, String customMessage) {
    this(errorCode, customMessage, null);
  }

  public ApiException(ErrorCode errorCode, Throwable cause) {
    this(errorCode, errorCode.getMessage(), cause);
  }

  public ApiException(ErrorCode errorCode, String customMessage, Throwable cause) {
    super(customMessage, cause);
    this.errorCode = errorCode;
    this.detailedMessage = customMessage;
  }
}
