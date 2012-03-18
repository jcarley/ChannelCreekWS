package com.channelcreek.infrastructure.tasks;

import com.channelcreek.webservices.model.HibernateUtil;
import org.hibernate.Session;

/**
 *
 * @author Jefferson Carley
 */
public class TaskExecutor {

  public static void executeTask(BaseTask task) {
    Session session = HibernateUtil.getSessionFactory().openSession();

    try {
      task.Run(session);
    } finally {
      if(session.isOpen())
        session.close();
    }

  }
}
