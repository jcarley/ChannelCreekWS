package com.channelcreek.webservices.tasks;

import com.channelcreek.infrastructure.tasks.BaseTask;
import com.channelcreek.webservices.model.Player;
import com.channelcreek.webservices.model.Team;

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
  private boolean successful;

  public SubmitPlayerToTeamRosterTask(long teamId, String name, int jerseyNumber, String position) {
    this.teamId = teamId;
    this.name = name;
    this.jerseyNumber = jerseyNumber;
    this.position = position;
    this.successful = false;
  }

  @Override
  public void Execute() {
    Team team = (Team)getSession().get(Team.class, teamId);

    if(team == null) {
      this.successful = false;
      return;
    }

    player = new Player(name, true, jerseyNumber, position);

    team.getPlayers().add(player);
    player.setTeam(team);

    getSession().saveOrUpdate(team);

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
