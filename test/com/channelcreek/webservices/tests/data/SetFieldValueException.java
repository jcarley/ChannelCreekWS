package com.channelcreek.webservices.tests.data;

/**
 *
 * @author Jefferson Carley
 */
public class SetFieldValueException extends Exception {

  public SetFieldValueException(String message) {
    super(message);
  }

  public SetFieldValueException(Throwable error) {
    super(error);
  }

  public SetFieldValueException(String message, Exception error) {
    super(message, error);
  }

}
