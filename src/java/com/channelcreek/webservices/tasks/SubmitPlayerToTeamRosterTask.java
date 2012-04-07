package com.channelcreek.webservices.tasks;

import com.channelcreek.infrastructure.tasks.BaseTask;
import com.channelcreek.webservices.model.Player;
import com.channelcreek.webservices.model.Team;
import javax.persistence.EntityNotFoundException;
import org.hibernate.Session;

/**
 * Adds a new player to the League.  Every player must belong to a team.
 *
 * @author Jefferson Carley
 */
public class SubmitPlayerToTeamRosterTask extends BaseTask {

  private Player player;
  private long teamId;
  private String name;
  private int jerseyNumber;
  private String position;
  private boolean active;

  public SubmitPlayerToTeamRosterTask(long teamId, String name, int jerseyNumber, String position, boolean active) {
    this.teamId = teamId;
    this.name = name;
    this.jerseyNumber = jerseyNumber;
    this.position = position;
    this.active = active;
  }

  @Override
  public void Execute() throws Exception {

    Session session = getSession();

    Team team = (Team)session.get(Team.class, teamId);

    if(team == null) {
      throw new EntityNotFoundException("Unable to find team with id '" + this.teamId + "'");
    }

    player = new Player(name, true, jerseyNumber, position);
    player.setActive(this.active);

    team.addPlayer(player);

    session.saveOrUpdate(team);
  }

  @Override
  public void OnError(Exception e) {
    player = null;
    super.OnError(e);
  }

  public Player getPlayer() {
    return this.player;
  }
}
