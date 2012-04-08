package com.channelcreek.webservices.tests.tasks;

import com.channelcreek.infrastructure.tasks.TaskExecutor;
import com.channelcreek.webservices.model.Game;
import com.channelcreek.webservices.model.HibernateUtil;
import com.channelcreek.webservices.model.Schedule;
import com.channelcreek.webservices.model.Team;
import com.channelcreek.webservices.tasks.ReportGameScoresTask;
import com.channelcreek.webservices.tasks.RetrieveTeamScheduleTask;
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
public class ScheduleTasksTests {

  final int EXPECTED_GAMES = 5;
  final String EXPECTED_SEASON_NAME = "Season-2010";

  private Team team;
  private Schedule schedule;
  private List<Game> games;

  public ScheduleTasksTests() {
  }

  @Before
  public void setUp() {

    // Arrange
    team = DbFactory.build(Team.class);
    schedule = DbFactory.build(Schedule.class);

    games = DbFactory.sequence(Game.class, EXPECTED_GAMES, new SequencePropertyOverrides<Game>() {

      @Override
      public void override(Game obj, int index) {
        obj.setLocation("Game " + index);
        obj.setGameDate("5-" + index + "-2012");
        obj.setHomeFinalScore(index + 2);
        obj.setVisitorFinalScore(index - 1);
      }
    });

    team.addSchedule(schedule);

    for(Game game : games) {
      schedule.addGame(game);
    }

    Session session = HibernateUtil.getSessionFactory().openSession();
    DbFactory.save(team, session);
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testShouldRetrieveATeamsSeasonSchedule() {

    // Act
    RetrieveTeamScheduleTask retrieveTeamScheduleTask = new RetrieveTeamScheduleTask(team.getTeamId(), EXPECTED_SEASON_NAME);
    TaskExecutor.executeTask(retrieveTeamScheduleTask);

    // Assert
    assertNotNull(retrieveTeamScheduleTask.getSchedule());
    assertTrue(retrieveTeamScheduleTask.isSuccessful());
    assertEquals(EXPECTED_GAMES, retrieveTeamScheduleTask.getSchedule().getGames().size());
    assertEquals(EXPECTED_SEASON_NAME, retrieveTeamScheduleTask.getSchedule().getSeasonName());

  }

  @Test
  public void testShouldNotRetrieveATeamsSeasonSchedule_EntityNotFound_InvalidTeamId() {

    // Act
    RetrieveTeamScheduleTask retrieveTeamScheduleTask = new RetrieveTeamScheduleTask(99, EXPECTED_SEASON_NAME);
    TaskExecutor.executeTask(retrieveTeamScheduleTask);

    // Assert
    assertNull(retrieveTeamScheduleTask.getSchedule());
    assertFalse(retrieveTeamScheduleTask.isSuccessful());
  }

  @Test
  public void testShouldNotRetrieveATeamsSeasonSchedule_EntityNotFound_InvalidSeasonName() {

    // Act
    RetrieveTeamScheduleTask retrieveTeamScheduleTask = new RetrieveTeamScheduleTask(team.getTeamId(), "Season-1900");
    TaskExecutor.executeTask(retrieveTeamScheduleTask);

    // Assert
    assertNull(retrieveTeamScheduleTask.getSchedule());
    assertFalse(retrieveTeamScheduleTask.isSuccessful());
  }

  @Test
  public void testShouldReportGameScores() {

    Game game = games.get(0);

    // Act
    ReportGameScoresTask reportGameScoresTask = new ReportGameScoresTask(game.getGameId());
    TaskExecutor.executeTask(reportGameScoresTask);

    // Assert
    assertNotNull(reportGameScoresTask.getGame());
    assertTrue(reportGameScoresTask.isSuccessful());
    assertEquals(game.getHomeFinalScore(), reportGameScoresTask.getGame().getHomeFinalScore());
    assertEquals(game.getVisitorFinalScore(), reportGameScoresTask.getGame().getVisitorFinalScore());
  }

  @Test
  public void testShouldNotReportGameScore_EntityNotFound() {

    // Act
    ReportGameScoresTask reportGameScoresTask = new ReportGameScoresTask(99);
    TaskExecutor.executeTask(reportGameScoresTask);

    // Assert
    assertNull(reportGameScoresTask.getGame());
    assertFalse(reportGameScoresTask.isSuccessful());
  }

}
