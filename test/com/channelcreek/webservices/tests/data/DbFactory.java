package com.channelcreek.webservices.tests.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
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

  public static void save(List<? extends Object> list, Session session) {
    Transaction transaction = session.beginTransaction();
    for(int index = 0; index < list.size(); index++) {
      session.saveOrUpdate(list.get(index));
    }
    transaction.commit();
    session.close();
  }

  public static void save(List<? extends Object> list, Session session, boolean leaveSessionOpen) {
    Transaction transaction = session.beginTransaction();
    for(int index = 0; index < list.size(); index++) {
      session.saveOrUpdate(list.get(index));
    }
    transaction.commit();

    if(!leaveSessionOpen)
      session.close();
  }

  public static void save(Object obj, Session session) {
    Transaction transaction = session.beginTransaction();
    session.saveOrUpdate(obj);
    transaction.commit();
    session.close();
  }

  public static void save(Object obj, Session session, boolean leaveSessionOpen) {
    Transaction transaction = session.beginTransaction();
    session.saveOrUpdate(obj);
    transaction.commit();

    if(!leaveSessionOpen)
      session.close();
  }

  public static <T extends Object> T build(Class<T> clazz) {
    return build(clazz, null);
  }

  public static <T extends Object> T build(Class<T> clazz, BuildPropertyOverrides<T> action) {

    DynamicClassDescriptor<T> dcd = buildClassDescriptor(clazz);
    T newInstance = getNewInstance(dcd);

    if (newInstance != null && action != null) {
      action.override(newInstance);
    }

    return newInstance;
  }

  public static <T extends Object> List<T> sequence(Class<T> clazz, int count) {
    return sequence(clazz, count, null);
  }

  public static <T extends Object> List<T> sequence(Class<T> clazz, int count, SequencePropertyOverrides<T> action) {

    LinkedList<T> list = new LinkedList<T>();

    for (int index = 0; index < count; index++) {

      DynamicClassDescriptor<T> dcd = buildClassDescriptor(clazz);
      T newInstance = getNewInstance(dcd);

      if (newInstance != null && action != null) {
        action.override(newInstance, index);
      }

      list.add(newInstance);
    }

    return list;
  }

  private static <T extends Object> DynamicClassDescriptor buildClassDescriptor(Class<T> clazz) {
    final DynamicClassDescriptor<T> dcd = new DynamicClassDescriptor<T>(clazz);

    dcd.setPropertyBuilder(new DynamicFieldBuilder() {

      @Override
      public Object getPropertyValueFor(String propertyName) {

        // get the lower case class name without the package name
        String className = dcd.getCollapsedName();

        Object value = null;

        // does the map contain a definition for this class
        if (map.containsKey(className)) {

          // the class definition
          LinkedHashMap entry = (LinkedHashMap) map.get(className);

          if (entry != null) {

            // is there a property definition for propertyName
            if (entry.containsKey(propertyName)) {

              // get the value for this property
              value = entry.get(propertyName);
            }
          }
        }

        return value;
      }
    });

    return dcd;
  }

  private static <T extends Object> T getNewInstance(DynamicClassDescriptor<T> dcd) {
    T newInstance = null;
    try {
      newInstance = (T) dcd.newInstance();
    } catch (Exception ex) {
      //TODO: Better handle an exception here.
      Logger.getLogger(DbFactory.class.getName()).log(Level.SEVERE, null, ex);
    }

    return newInstance;
  }

  private static void load(String yamlFile) throws FileNotFoundException {
    Yaml yaml = new Yaml();
    FileInputStream input = new FileInputStream(yamlFile);
    Iterator<Object> iterator = yaml.loadAll(input).iterator();
    map = (LinkedHashMap) iterator.next();
  }
}
