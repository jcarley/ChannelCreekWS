package com.channelcreek.webservices.model;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.dialect.Dialect;
import org.hibernate.tool.hbm2ddl.SchemaExport;

/**
 * Hibernate Utility class with a convenient method to get Session Factory object.
 *
 * @author Jefferson Carley
 */
public class HibernateUtil {

  private static AnnotationConfiguration configuration = null;
  private static SessionFactory sessionFactory = null;

  static {
    try {
      // Create the SessionFactory from standard (hibernate.cfg.xml)
      // config file.
      configuration = new AnnotationConfiguration()
              .setNamingStrategy(ImprovedNamingStrategy.INSTANCE)
              .configure();
      
    } catch (Throwable ex) {
      // Log the exception.
      System.err.println("Initial SessionFactory creation failed." + ex);
      throw new ExceptionInInitializerError(ex);
    }
  }

  public static SessionFactory getSessionFactory() {
    if(sessionFactory == null) {
      sessionFactory = configuration.buildSessionFactory();
    }
    return sessionFactory;
  }

  public static void updateDatabaseScheme() {

    // we have to destroy any previous session factories before
    // updating the database schema.  the sessionFactory will be
    // recreated the next time its requested
    if(sessionFactory != null) {
      if(!sessionFactory.isClosed()) {
        sessionFactory.close();
      }
      sessionFactory = null;
    }

    SchemaExport exporter = new SchemaExport(configuration);

    // first drop
    exporter.execute(true, true, true, false);

    // then create
    exporter.execute(true, true, false, true);
  }

  /*
   * Generates the database creation scripts and saves them to disk
   */
  public static void generateDatabaseCreationScript(String scriptFile) throws FileNotFoundException {
    Dialect dialect = Dialect.getDialect(configuration.getProperties());
    String[] generateSchemaCreationScript = configuration.generateSchemaCreationScript(dialect);
    PrintWriter pw = null;
    try {
      pw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(scriptFile)));
      for(String line : generateSchemaCreationScript) {
        pw.println(line);
      }
    } finally {
      if(pw != null) {
        pw.flush();
        pw.close();
      }
    }
  }

  /*
   * A convienance method used for debugging
   */
  private static void verifyMappings() {
    Iterator iterator = configuration.getClassMappings();
    while(iterator.hasNext()) {
      System.out.println("*********** Class mapping Found: " + iterator.next());
    }
  }
}
