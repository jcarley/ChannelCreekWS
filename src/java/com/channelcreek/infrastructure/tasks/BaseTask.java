package com.channelcreek.infrastructure.tasks;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Jefferson Carley
 */
public abstract class BaseTask {

  private Session session;
  private Transaction transaction;

  protected void Initialize(Session session) {
    this.session = session;
  }

  public abstract void Execute();

  protected void OnError(Exception e) {
  }

  public boolean Run(Session session) {

    Initialize(session);

    try {
      beginTransaction();
      Execute();
      commitTransaction();
    } catch (HibernateException e) {
      rollbackTransaction();
      OnError(e);
      return false;
    } catch (Exception e) {
      rollbackTransaction();
      OnError(e);
      return false;
    }

    return true;
  }

  protected Session getSession() {
    return this.session;
  }

  protected Transaction getTransaction() {
    return this.transaction;
  }

  private void beginTransaction() {
    this.transaction = getSession().beginTransaction();
  }

  private void commitTransaction() {
    getTransaction().commit();
  }

  private void rollbackTransaction() {
    getTransaction().rollback();
  }
}
