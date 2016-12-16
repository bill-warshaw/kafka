package org.apache.kafka.common.errors;

public class ExpectedOffsetIncorrectException extends RetriableException {
  private static final long serialVersionUID = 1L;

  public ExpectedOffsetIncorrectException(String message) {
    super(message);
  }

}
