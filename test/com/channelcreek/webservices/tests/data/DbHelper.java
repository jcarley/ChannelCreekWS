package com.channelcreek.webservices.tests.data;

import com.channelcreek.webservices.model.HibernateUtil;
import com.channelcreek.webservices.tests.data.builders.StatementFactory;
import com.channelcreek.webservices.tests.data.mappers.TableMapper;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.reader.UnicodeReader;

/**
 *
 * @author Jefferson Carley
 */
public class DbHelper {

  public static void main(String[] args) {
    new DbHelper();
  }

  private final static String DATA_FILE_NAME = "data.yml";
  private static ArrayList<String> insertScripts = new ArrayList<String>();
  private static ArrayList<String> deleteScripts = new ArrayList<String>();

  static {
    try {
//      HibernateUtil.updateDatabaseScheme();
      loadFromYaml();
    } catch (FileNotFoundException ex) {
      Logger.getLogger(DbHelper.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public static void setUp() {

    Session session = HibernateUtil.getSessionFactory().openSession();
    session.beginTransaction();
    try {
      for (String insertScript : insertScripts) {
        SQLQuery insertStatement = session.createSQLQuery(insertScript);
        insertStatement.executeUpdate();
      }
      session.getTransaction().commit();
    } finally {
      session.close();
    }
  }

  public static void tearDown() {
    Session session = HibernateUtil.getSessionFactory().openSession();
    session.beginTransaction();

    try {
      for(int i = deleteScripts.size() - 1; i >= 0; i--) {
        String deleteScript = deleteScripts.get(i);
        SQLQuery insertStatement = session.createSQLQuery(deleteScript);
        insertStatement.executeUpdate();
      }
      session.getTransaction().commit();
    } finally {
      session.close();
    }
  }

  private static void loadFromYaml() throws FileNotFoundException {

    Yaml yaml = new Yaml();
    FileInputStream input = new FileInputStream(DATA_FILE_NAME);
    UnicodeReader reader = new UnicodeReader(input);

    MappingNode mappingNode = (MappingNode) yaml.compose(reader);

    Iterator<NodeTuple> iterator = mappingNode.getValue().iterator();

    ArrayList<String> usedTableNames = new ArrayList<String>();

    while (iterator.hasNext()) {
      NodeTuple node = iterator.next();

      TableMapper table = new TableMapper(node);

      StatementFactory factory = new StatementFactory();

      String insert = factory.Insert(table);
      insertScripts.add(insert);

      if (!usedTableNames.contains(table.getName())) {
        String delete = factory.Delete(table);
        deleteScripts.add(delete);
        usedTableNames.add(table.getName());
      }
    }

  }

  private static Iterator<Object> load(String yamlFile) throws FileNotFoundException {

    Yaml yaml = new Yaml();

    yaml.setBeanAccess(BeanAccess.FIELD);
    FileInputStream input = new FileInputStream(yamlFile);
    Iterator<Object> iterator = yaml.loadAll(input).iterator();

    return iterator;
  }
}
