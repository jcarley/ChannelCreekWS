package com.channelcreek.infrastructure.tasks;

import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Jefferson Carley
 */
public abstract class BaseTask {

  private Session session;
  private Transaction transaction;
  private Exception exception;
  private boolean successful = false;

  protected void Initialize(Session session) {
    this.session = session;
  }

  public abstract void Execute() throws Exception;

  protected void OnError(Exception e) {
    this.exception = e;
  }

  public boolean Run(Session session) {

    Initialize(session);

    try {
      beginTransaction();
      Execute();
      commitTransaction();
      setSuccessful(true);
    } catch (Exception e) {
      rollbackTransaction();
      OnError(e);
      setSuccessful(false);
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

  public boolean isSuccessful() {
    return this.successful;
  }

  private void setSuccessful(boolean successful) {
    this.successful = successful;
  }

  public Exception getException() {
    return exception;
  }
}
