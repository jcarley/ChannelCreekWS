package com.channelcreek.webservices.tests.tasks;

import com.channelcreek.infrastructure.tasks.TaskExecutor;
import com.channelcreek.webservices.model.Game;
import com.channelcreek.webservices.model.HibernateUtil;
import com.channelcreek.webservices.model.Schedule;
import com.channelcreek.webservices.model.Team;
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

  public ScheduleTasksTests() {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testShouldRetrieveATeamsSeasonSchedule() {
    // Arrange
    final int EXPECTED_GAMES = 5;
    final String EXPECTED_SEASON_NAME = "Season-2010";

    Team team = DbFactory.build(Team.class);
    Schedule schedule = DbFactory.build(Schedule.class);

    List<Game> games = DbFactory.sequence(Game.class, EXPECTED_GAMES, new SequencePropertyOverrides<Game>() {

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

    // Act
    RetrieveTeamScheduleTask retrieveTeamScheduleTask = new RetrieveTeamScheduleTask(team.getTeamId(), EXPECTED_SEASON_NAME);
    TaskExecutor.executeTask(retrieveTeamScheduleTask);

    // Assert
    assertNotNull(retrieveTeamScheduleTask.getSchedule());
    assertEquals(EXPECTED_GAMES, retrieveTeamScheduleTask.getSchedule().getGames().size());
    assertEquals(EXPECTED_SEASON_NAME, retrieveTeamScheduleTask.getSchedule().getSeasonName());

  }

}
