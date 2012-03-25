package com.channelcreek.webservices.tasks;

import com.channelcreek.infrastructure.tasks.BaseTask;
import com.channelcreek.webservices.model.HibernateUtil;
import com.channelcreek.webservices.model.Player;
import com.channelcreek.webservices.model.Team;
import java.util.ArrayList;
import java.util.Collections;
import org.hibernate.Session;

/**
 *
 * @author Jefferson Carley
 */
public class FindActivePlayersTask extends BaseTask {

  private long teamId;
  private ArrayList<Player> activePlayers = new ArrayList<Player>();

  public FindActivePlayersTask(long teamId) {
    this.teamId = teamId;
  }

  public ArrayList<Player> getPlayers() {
    return this.activePlayers;
  }

  @Override
  public void Execute() {

    Session session = HibernateUtil.getSessionFactory().openSession();
    Team team = (Team)session.get(Team.class, this.teamId);

    Collections.copy(team.getActivePlayers(), this.activePlayers);

    session.close();
  }

}
