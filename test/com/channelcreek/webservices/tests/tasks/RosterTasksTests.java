package com.channelcreek.webservices.tests.tasks;

import com.channelcreek.infrastructure.tasks.TaskExecutor;
import com.channelcreek.webservices.model.HibernateUtil;
import com.channelcreek.webservices.model.Player;
import com.channelcreek.webservices.model.Team;
import com.channelcreek.webservices.tasks.ActivatePlayerTask;
import com.channelcreek.webservices.tasks.FindActivePlayersTask;
import com.channelcreek.webservices.tasks.FindActiveTeamsTask;
import com.channelcreek.webservices.tasks.SubmitPlayerToTeamRosterTask;
import com.channelcreek.webservices.tests.data.BuildPropertyOverrides;
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

    // Arrange
    final int EXPECTED_ACTIVE_PLAYERS = 5;

    Team team = DbFactory.build(Team.class, null);

    // we are going to add 6 players to this team, but the only the last 5 players are going to be active
    List<Player> players = DbFactory.sequence(Player.class, EXPECTED_ACTIVE_PLAYERS + 1, new SequencePropertyOverrides<Player>() {

      @Override
      public void override(Player obj, int index) {
        obj.setName("Player " + index);
        if(index == 0) {
          obj.setActive(false);
        }
      }
    });

    for(Player player : players) {
      team.addPlayer(player);
    }

    Session session = HibernateUtil.getSessionFactory().openSession();
    DbFactory.save(team, session);

    // Act
    FindActivePlayersTask findActivePlayersTask = new FindActivePlayersTask(team.getTeamId());
    TaskExecutor.executeTask(findActivePlayersTask);

    // Assert
    assertEquals(EXPECTED_ACTIVE_PLAYERS, findActivePlayersTask.getPlayers().size());

    for(Player player : findActivePlayersTask.getPlayers()) {
      assertTrue(player.isActive());
    }
  }

  @Test
  public void testSubmitPlayerToTeamRoster() {

    // Arrange
    String name = "Ryan Braun";
    int jerseyNumber = 64;
    String position = "Leftfield";
    boolean active = true;

    Team team = DbFactory.build(Team.class, null);
    Session session = HibernateUtil.getSessionFactory().openSession();
    DbFactory.save(team, session);


    // Act
    SubmitPlayerToTeamRosterTask submitPlayerToTeamRosterTask = new SubmitPlayerToTeamRosterTask(
            team.getTeamId(),
            name,
            jerseyNumber,
            position,
            active);

    TaskExecutor.executeTask(submitPlayerToTeamRosterTask);

    // Assert
    assertTrue("Task was not successful", submitPlayerToTeamRosterTask.isSuccessful());
    assertNotNull("Player was null", submitPlayerToTeamRosterTask.getPlayer());
    assertTrue("PlayerId should be greater than 0", submitPlayerToTeamRosterTask.getPlayer().getPlayerId() > 0);
    assertTrue("Player should be active but is not", submitPlayerToTeamRosterTask.getPlayer().isActive());
  }

  @Test
  public void testActivateExistingPlayer_RosterMaxNotMet() {

    // Arrange
    Team team = DbFactory.build(Team.class, null);
    Player player = DbFactory.build(Player.class, new BuildPropertyOverrides<Player>() {

      @Override
      public void override(Player obj) {
        obj.setActive(false);
      }
    });

    team.addPlayer(player);

    Session session = HibernateUtil.getSessionFactory().openSession();
    DbFactory.save(team, session);

    // Act
    ActivatePlayerTask activatePlayerTask = new ActivatePlayerTask(team.getTeamId(), player.getPlayerId());
    TaskExecutor.executeTask(activatePlayerTask);

    // Assert
    assertTrue(activatePlayerTask.isSuccessful());
    assertNotNull(activatePlayerTask.getPlayer());
    assertTrue(activatePlayerTask.getPlayer().isActive());
  }

  @Test
  public void testActivateExistingPlayer_RosterMaxExceeded() {

    // Arrange
    final int EXPECTED_ACTIVE_PLAYERS = 15;

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

    for(Player player : players) {
      team.addPlayer(player);
    }

    Session session = HibernateUtil.getSessionFactory().openSession();
    DbFactory.save(team, session);

    // Act
    ActivatePlayerTask activatePlayerTask = new ActivatePlayerTask(team.getTeamId(), players.get(0).getPlayerId());
    TaskExecutor.executeTask(activatePlayerTask);

    // Assert
    assertFalse("Task was not successful", activatePlayerTask.isSuccessful());
    assertNotNull("Player was null", activatePlayerTask.getPlayer());
    assertFalse("Play should be active but is not", activatePlayerTask.getPlayer().isActive());
  }
}
