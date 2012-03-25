package com.channelcreek.webservices.tests.tasks;

import com.channelcreek.infrastructure.tasks.TaskExecutor;
import com.channelcreek.webservices.model.HibernateUtil;
import com.channelcreek.webservices.model.Player;
import com.channelcreek.webservices.model.Team;
import com.channelcreek.webservices.tasks.ActivatePlayerTask;
import com.channelcreek.webservices.tasks.FindActivePlayersTask;
import com.channelcreek.webservices.tasks.FindActiveTeamsTask;
import com.channelcreek.webservices.tasks.SubmitPlayerToTeamRosterTask;
import com.channelcreek.webservices.tests.data.DbFactory;
import com.channelcreek.webservices.tests.data.SequencePropertyOverrides;
import java.util.List;
import org.hibernate.Session;
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
    HibernateUtil.updateDatabaseScheme();
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testShouldFindAllActiveTeams() {

    // expected result
    final int EXPECTED_ACTIVE_TEAMS = 5;

    // Arrange
    List<Team> teams = DbFactory.sequence(Team.class, EXPECTED_ACTIVE_TEAMS, new SequencePropertyOverrides<Team>() {

      @Override
      public void override(Team obj, int index) {
        obj.setName("Team " + index);
      }

    });

    Session session = HibernateUtil.getSessionFactory().openSession();
    DbFactory.save(teams, session);

    // Act
    FindActiveTeamsTask findActiveTeamsTask = new FindActiveTeamsTask();
    TaskExecutor.executeTask(findActiveTeamsTask);


    // Assert
    assertEquals(EXPECTED_ACTIVE_TEAMS, findActiveTeamsTask.getActiveTeams().size());

  }

  @Test
  public void testShouldFindAllActivePlayers() {

    final int EXPECTED_ACTIVE_PLAYERS = 5;

    Team team = DbFactory.build(Team.class, null);
    List<Player> players = DbFactory.sequence(Player.class, EXPECTED_ACTIVE_PLAYERS + 1, new SequencePropertyOverrides<Player>() {

      @Override
      public void override(Player obj, int index) {
        obj.setName("Player " + index);
        if(index == 0) {
          obj.setActive(false);
        }
      }
    });

//    team.getPlayers().addAll(players);
    Session session = HibernateUtil.getSessionFactory().openSession();
    DbFactory.save(team, session);

    FindActivePlayersTask findActivePlayersTask = new FindActivePlayersTask(team.getTeamId());

    TaskExecutor.executeTask(findActivePlayersTask);

    assertEquals(EXPECTED_ACTIVE_PLAYERS, findActivePlayersTask.getPlayers().size());

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

    long teamId = 2;
    long playerId = 3;

    ActivatePlayerTask activatePlayerTask = new ActivatePlayerTask(teamId, playerId);

    TaskExecutor.executeTask(activatePlayerTask);

    assertFalse("Task was not successful", activatePlayerTask.isSuccessful());
    assertNotNull("Player was null", activatePlayerTask.getPlayer());
    assertFalse("Play should be active but is not", activatePlayerTask.getPlayer().isActive());
  }
}
