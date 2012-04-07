package com.channelcreek.webservices.tasks;

import com.channelcreek.infrastructure.tasks.BaseTask;
import com.channelcreek.webservices.model.Team;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Jefferson Carley
 */
public class FindActiveTeamsTask extends BaseTask {

  private List<Team> activeTeams = new ArrayList<Team>();

  public List<Team> getActiveTeams() {
    return this.activeTeams;
  }

  @Override
  public void Execute() throws Exception {

    Session session = super.getSession();
    Criteria criteria = session.createCriteria(Team.class);
    Query query = session.createQuery("from Team where active = true");
    List list = query.list();
    Iterator iterator = list.iterator();

    while(iterator.hasNext()) {
      activeTeams.add((Team)iterator.next());
    }
  }

}
