package com.channelcreek.webservices.tasks;

import com.channelcreek.infrastructure.tasks.BaseTask;
import com.channelcreek.webservices.model.Player;
import java.util.ArrayList;

/**
 *
 * @author Jefferson Carley
 */
public class FindActivePlayersTask extends BaseTask {

  private int teamId;
  private ArrayList<Player> activePlayers = new ArrayList<Player>();

  public FindActivePlayersTask(int teamId) {
    this.teamId = teamId;
  }

  public ArrayList<Player> getPlayers() {
    return this.activePlayers;
  }

  @Override
  public void Execute() {
    
    for(int c = 0; c < 5; c++) {
      Player player = new Player();
      player.setName("Joe");
      player.setActive(true);
      player.setJerseyNumber(c);
      player.setPosition(Integer.toString(c));

      activePlayers.add(player);
    }
  }

}
