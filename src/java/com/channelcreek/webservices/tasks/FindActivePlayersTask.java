package com.channelcreek.webservices.tasks;

import com.channelcreek.infrastructure.tasks.BaseTask;
import com.channelcreek.webservices.model.Player;
import com.channelcreek.webservices.model.Team;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author Jefferson Carley
 */
public class FindActivePlayersTask extends BaseTask {

  private long teamId;
  private List<Player> activePlayers;

  public FindActivePlayersTask(long teamId) {
    this.teamId = teamId;
  }

  public List<Player> getPlayers() {
    return this.activePlayers;
  }

  @Override
  public void Execute() {

    // we get the hibernate session from the base class.  We don't
    // have to worry about managing it.  The infrastructure will handle
    // that for us.
    Session session = getSession();

    Team team = (Team)session.get(Team.class, this.teamId);

    this.activePlayers = new ArrayList<Player>(team.getActivePlayers());
  }

}
