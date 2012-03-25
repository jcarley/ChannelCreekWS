package com.channelcreek.webservices.tests.data;

/**
 *
 * @author Jefferson Carley
 */
public class BuildInstanceException extends Exception {

  public BuildInstanceException(String message) {
    super(message);
  }

  public BuildInstanceException(Throwable error) {
    super(error);
  }

  public BuildInstanceException(String message, Throwable error) {
    super(message, error);
  }
}
