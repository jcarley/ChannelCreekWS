package com.channelcreek.webservices.tests.data;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Jefferson Carley
 */
public class DynamicClassDescriptor<T extends Object> {

  private T instance = null;
  private Class<T> clazz = null;
  private DynamicFieldBuilder builder = null;
  private Map<String, DynamicFieldDescriptor> fieldMap = null;

  public DynamicClassDescriptor(Class<T> clazz) {
    this.clazz = clazz;
    this.fieldMap = new HashMap<String, DynamicFieldDescriptor>();
    discoverFields();
  }

  public void setPropertyBuilder(DynamicFieldBuilder builder) {
    this.builder = builder;
  }

  public T newInstance() throws BuildInstanceException, SetFieldValueException, NullPointerException {
    buildInstance();

    for(String propertyName : fieldMap.keySet()) {
      DynamicFieldDescriptor fieldDescriptor = this.fieldMap.get(propertyName);

      if(fieldDescriptor == null) {
        continue;
      }

      Object value = this.builder.getPropertyValueFor(propertyName);
      if(value != null) {
        fieldDescriptor.setValue(this.instance, value);
      }
    }

    return this.instance;
  }

  public String getClassName() {
    return this.clazz.getCanonicalName();
  }

  public String getCollapsedName() {
    
    // match only the class name
    Pattern expression = Pattern.compile("([^.]*)$");
    Matcher matcher = expression.matcher(getClassName());

    if(matcher.find())
      return matcher.group().toLowerCase();

    return "<unknown>";
  }

  private void discoverFields() {
    Method[] declaredMethods = this.clazz.getDeclaredMethods();
    for(Method method : declaredMethods) {
      if(isFieldAccessor(method)) {
        addToFieldMap(method);
      }
    }
  }

  private void addToFieldMap(Method method) {
    String name = DynamicFieldDescriptor.collapseName(method);

    if(!this.fieldMap.containsKey(name)) {
      this.fieldMap.put(name, new DynamicFieldDescriptor());
    }

    this.fieldMap.get(name).appendAccessor(method);
  }

  private boolean isFieldAccessor(Method method) {
    return DynamicFieldDescriptor.isAccessor(method);
  }

  private void buildInstance() throws BuildInstanceException {
    try {
      Constructor<T> ctor = this.clazz.getConstructor();
      this.instance = ctor.newInstance();
    } catch (Exception ex) {
      throw new BuildInstanceException("Unable to create instance of '" + this.clazz.getCanonicalName()  + "'", ex);
    }
  }

}
