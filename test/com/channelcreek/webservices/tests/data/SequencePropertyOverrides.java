package com.channelcreek.webservices.tests.data;

/**
 *
 * @author Jefferson Carley
 */
public interface SequencePropertyOverrides<T extends Object> {
  public void override(T obj, int index);
}
