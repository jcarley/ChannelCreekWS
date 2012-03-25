package com.channelcreek.webservices.tests.tasks;

import com.channelcreek.infrastructure.tasks.TaskExecutor;
import com.channelcreek.webservices.model.Player;
import com.channelcreek.webservices.tasks.ActivatePlayerTask;
import com.channelcreek.webservices.tasks.FindActivePlayersTask;
import com.channelcreek.webservices.tasks.FindActiveTeamsTask;
import com.channelcreek.webservices.tasks.SubmitPlayerToTeamRosterTask;
import com.channelcreek.webservices.tests.data.DbFactory;
import com.channelcreek.webservices.tests.data.PropertyOverrides;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Jefferson Carley
 */
public class RosterTasksTests {

  public RosterTasksTests() {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testShouldFindAllActiveTeams() {

    final int EXPECTED_ACTIVE_TEAMS = 5;

    FindActiveTeamsTask findActiveTeamsTask = new FindActiveTeamsTask();

    TaskExecutor.executeTask(findActiveTeamsTask);

    assertEquals(findActiveTeamsTask.getActiveTeams().size(), EXPECTED_ACTIVE_TEAMS);

  }

  @Test
  public void testShouldFindAllActivePlayers() {

    final int EXPECTED_ACTIVE_PLAYERS = 5;
    final int TEAM_ID = 1;

    FindActivePlayersTask findActivePlayersTask = new FindActivePlayersTask(TEAM_ID);

    TaskExecutor.executeTask(findActivePlayersTask);

    assertEquals(findActivePlayersTask.getPlayers().size(), EXPECTED_ACTIVE_PLAYERS);

    for(Player player : findActivePlayersTask.getPlayers()) {
      assertTrue(player.isActive());
    }
  }

  @Test
  public void testSubmitPlayerToTeamRoster() {
    long teamId = 1;
    String name = "Ryan Braun";
    int jerseyNumber = 64;
    String position = "Leftfield";

    SubmitPlayerToTeamRosterTask submitPlayerToTeamRosterTask = new SubmitPlayerToTeamRosterTask(
            teamId,
            name,
            jerseyNumber,
            position);

    TaskExecutor.executeTask(submitPlayerToTeamRosterTask);

    assertTrue("Task was not successful", submitPlayerToTeamRosterTask.isSuccessful());
    assertNotNull("Player was null", submitPlayerToTeamRosterTask.getPlayer());
    assertTrue("PlayerId should be greater than 0", submitPlayerToTeamRosterTask.getPlayer().getPlayerId() > 0);
    assertTrue("Player should be active but is not", submitPlayerToTeamRosterTask.getPlayer().isActive());
  }

  @Test
  public void testActivateExistingPlayer_RosterMaxNotMet() {
    long teamId = 1;
    long playerId = 2;

    ActivatePlayerTask activatePlayerTask = new ActivatePlayerTask(teamId, playerId);

    TaskExecutor.executeTask(activatePlayerTask);

    assertTrue(activatePlayerTask.isSuccessful());
    assertNotNull(activatePlayerTask.getPlayer());
    assertTrue(activatePlayerTask.getPlayer().isActive());
  }

  @Test
  public void testActivateExistingPlayer_RosterMaxExceeded() {

    Player player = DbFactory.build(Player.class, new PropertyOverrides<Player>() {
      @Override
      public void override(Player obj) {
        obj.setJerseyNumber(65);
      }
    });

    long teamId = 2;
    long playerId = 3;

    ActivatePlayerTask activatePlayerTask = new ActivatePlayerTask(teamId, playerId);

    TaskExecutor.executeTask(activatePlayerTask);

    assertFalse("Task was not successful", activatePlayerTask.isSuccessful());
    assertNotNull("Player was null", activatePlayerTask.getPlayer());
    assertFalse("Play should be active but is not", activatePlayerTask.getPlayer().isActive());
  }
}
