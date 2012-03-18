package com.channelcreek.webservices.tasks;

import com.channelcreek.infrastructure.tasks.BaseTask;
import com.channelcreek.webservices.model.Team;
import java.util.ArrayList;

/**
 *
 * @author Jefferson Carley
 */
public class FindActiveTeamsTask extends BaseTask {

  private ArrayList<Team> activeTeams = new ArrayList<Team>();

  public ArrayList<Team> getActiveTeams() {
    return this.activeTeams;
  }

  @Override
  public void Execute() {
    
    String[] names = new String[] {
      "The Eagles",
      "The Zebras",
      "The Warhawks",
      "The Beagles",
      "The Ducks"
    };

    for(String name : names ) {

      Team team = new Team();
      team.setName(name);
      team.setActive(true);

      activeTeams.add(team);
    }

  }

}
