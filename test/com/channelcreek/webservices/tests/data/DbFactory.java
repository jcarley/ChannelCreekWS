package com.channelcreek.webservices.tests.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author Jefferson Carley
 */
public class DbFactory {

  public static void main(String[] args) {
    new DbFactory();
  }

  private final static String DATA_FILE_NAME = "data.yml";
  private static LinkedHashMap map = null;

  static {
    try {
      load(DATA_FILE_NAME);
    } catch (FileNotFoundException ex) {
      Logger.getLogger(DbFactory.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public static <T extends Object> T build(Class<T> clazz, PropertyOverrides<T> action) {

    final DynamicClassDescriptor dcd = new DynamicClassDescriptor(clazz);

    dcd.setPropertyBuilder(new DynamicFieldBuilder() {

      @Override
      public Object getPropertyValueFor(String propertyName) {

        // get the lower case class name without the package name
        String className = dcd.getCollapsedName();

        Object value = null;

        // does the map contain a definition for this class
        if(map.containsKey(className)) {

          // the class definition
          LinkedHashMap entry = (LinkedHashMap) map.get(className);

          if(entry != null) {

            // is there a property definition for propertyName
            if(entry.containsKey(propertyName)) {

              // get the value for this property
              value = entry.get(propertyName);
            }
          }
        }

        return value;
      }

    });

    T newInstance = null;
    try {
      newInstance = (T) dcd.newInstance();
    } catch (Exception ex) {
      Logger.getLogger(DbFactory.class.getName()).log(Level.SEVERE, null, ex);
    }

    if(newInstance != null && action != null) {
      action.override(newInstance);
    }

    return newInstance;
  }

  private static void load(String yamlFile) throws FileNotFoundException {
    Yaml yaml = new Yaml();
    FileInputStream input = new FileInputStream(yamlFile);
    Iterator<Object> iterator = yaml.loadAll(input).iterator();
    map = (LinkedHashMap)iterator.next();
  }

}
