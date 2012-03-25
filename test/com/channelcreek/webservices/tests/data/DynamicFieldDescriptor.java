package com.channelcreek.webservices.tests.data;

import java.lang.reflect.Method;

/**
 *
 * @author Jefferson Carley
 */
public class DynamicFieldDescriptor {

  private static final String SET = "set";
  private static final String GET = "get";

  private Method getAccessor = null;
  private Method setAccessor = null;

  public static String collapseName(Method method) {
    StringBuilder sb = new StringBuilder(method.getName());
    sb = sb.delete(0, 3);
    char c = sb.charAt(0);
    c = Character.toLowerCase(c);
    sb.setCharAt(0, c);
    return sb.toString();
  }

  public void setValue(Object obj, Object value) throws SetFieldValueException, NullPointerException {

    if(obj == null) {
      throw new NullPointerException("Can not set a value on a null object");
    }

    if(this.setAccessor == null) {

      String message;
      if(getAccessor != null) {
        message = "Empty setAccessor for " + collapseName(this.getAccessor) + "'";
      } else {
        message = "No setAccessor found for '" + obj.getClass().getCanonicalName() + "'";
      }

      throw new SetFieldValueException(message);
    }

    try {
      this.setAccessor.invoke(obj, value);
    } catch (Exception ex) {
      throw new SetFieldValueException("Unable to set value for '" + collapseName(this.setAccessor) + "'", ex);
    }
  }

  public static boolean isAccessor(Method method) {
    return isSetAccessor(method) || isGetAccessor(method);
  }

  public void appendAccessor(Method method) {
    if(isSetAccessor(method)) {
      this.setAccessor = method;
    } else {
      this.getAccessor = method;
    }
  }

  private static boolean isSetAccessor(Method method) {
    return method.getName().startsWith(SET);
  }

  private static boolean isGetAccessor(Method method) {
    return method.getName().startsWith(GET);
  }

}
