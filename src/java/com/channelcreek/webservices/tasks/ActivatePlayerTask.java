package com.channelcreek.webservices.tasks;

import com.channelcreek.infrastructure.tasks.BaseTask;
import com.channelcreek.webservices.model.Player;
import com.channelcreek.webservices.model.Team;

/**
 * Activates a player on a team.  The player must already exist in the system
 * and must belong to a team.  The max number of active players on a team roster
 * is set at 15.  Exceeding the number of active players on a team roster is an
 * error.  A team can have as many players as it wants, but only 15 can be active.
 *
 * @author Jefferson Carley
 */
public class ActivatePlayerTask extends BaseTask {

  private static final int MAX_ACTIVE_PLAYER_COUNT = 15;

  private Player player;
  private long teamId;
  private long playerId;
  private boolean successful;

  /**
   *
   * @param teamId The id of the team the player belongs to.
   * @param playerId The id of the player to activate
   */
  public ActivatePlayerTask(long teamId, long playerId) {
    this.teamId = teamId;
    this.playerId = playerId;
  }

  @Override
  public void Execute() {
    Team team = (Team)getSession().get(Team.class, this.teamId);

    if(team == null) {
      this.successful = false;
      return;
    }

    this.player = (Player)getSession().get(Player.class, this.playerId);

    if(this.player == null) {
      this.successful = false;
      return;
    }

    if(team.getActivePlayers().size() == MAX_ACTIVE_PLAYER_COUNT) {
      this.successful = false;
      return;
    }

    this.player.setActive(true);

    getSession().saveOrUpdate(player);

    this.successful = true;
  }

  public Player getPlayer() {
    return this.player;
  }

  public boolean isSuccessful() {
    return this.successful;
  }

}
