package com.channelcreek.webservices.tasks;

import com.channelcreek.infrastructure.tasks.BaseTask;
import com.channelcreek.webservices.model.Player;
import com.channelcreek.webservices.model.Team;
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
  private boolean successful;

  public SubmitPlayerToTeamRosterTask(long teamId, String name, int jerseyNumber, String position, boolean active) {
    this.teamId = teamId;
    this.name = name;
    this.jerseyNumber = jerseyNumber;
    this.position = position;
    this.active = active;
    this.successful = false;
  }

  @Override
  public void Execute() {

    Session session = getSession();

    Team team = (Team)session.get(Team.class, teamId);

    if(team == null) {
      this.successful = false;
      return;
    }

    player = new Player(name, true, jerseyNumber, position);
    player.setActive(this.active);

    team.addPlayer(player);

    session.saveOrUpdate(team);

    this.successful = true;
  }

  @Override
  public void OnError(Exception e) {
    player = null;
    super.OnError(e);
  }

  public Player getPlayer() {
    return this.player;
  }

  public boolean isSuccessful() {
    return this.successful;
  }

}
