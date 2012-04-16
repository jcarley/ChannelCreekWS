/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.channelcreek.webservices.tests.tasks;

import com.channelcreek.infrastructure.tasks.TaskExecutor;
import com.channelcreek.webservices.model.*;
import com.channelcreek.webservices.tasks.CalculateLeagueStandingsTask;
import com.channelcreek.webservices.tests.data.DbFactory;
import com.channelcreek.webservices.tests.data.SequencePropertyOverrides;
import java.util.List;
import org.hibernate.Session;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Jefferson
 */
public class LeagueStandingsTasksTests {

  private final int EXPECTED_ACTIVE_TEAMS = 6;
  private final int EXPECTED_GAMES_PLAYED = 12;

  private List<Team> teams = null;
  private List<Game> games = null;

  public LeagueStandingsTasksTests() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
    HibernateUtil.updateDatabaseScheme();
  }

  @Before
  public void setUp() {

    Session session = HibernateUtil.getSessionFactory().openSession();

    // setup 5 teams
    teams = DbFactory.sequence(Team.class, EXPECTED_ACTIVE_TEAMS, new SequencePropertyOverrides<Team>() {

      @Override
      public void override(Team obj, int index) {
        obj.setName("Team " + index);
      }

    });

    // each team has its own schedule
    for(Team team : teams) {
      Schedule schedule = DbFactory.build(Schedule.class);
      team.addSchedule(schedule);
    }

    DbFactory.save(teams, session, true);

    games = DbFactory.sequence(Game.class, EXPECTED_GAMES_PLAYED, new SequencePropertyOverrides<Game>() {

      @Override
      public void override(Game obj, int index) {
        obj.setGameDate("05/" + (index + 1) + "/2011");
        obj.setLocation("Location " + index);
      }

    });

    playGame(0, 3, 0, true);
    playGame(1, 4, 1, true);
    playGame(2, 5, 2, true);
    playGame(0, 5, 3, false);
    playGame(1, 4, 4, false);
    playGame(2, 3, 5, false);
    playGame(0, 1, 6, true);
    playGame(2, 3, 7, true);
    playGame(4, 5, 8, true);
    playGame(0, 2, 9, true);
    playGame(1, 3, 10, false);
    playGame(4, 5, 11, false);

    DbFactory.save(teams, session);

  }

  private void playGame(int homeTeamIndex, int visitorTeamIndex, int gameIndex, boolean homeTeamWins) {

    // get teams and game
    Team homeTeam = this.teams.get(homeTeamIndex);
    Team visitorTeam = this.teams.get(visitorTeamIndex);
    Game game = this.games.get(gameIndex);

    // set the game's home and visitor team
    game.setHomeTeam(homeTeam);
    game.setVisitorTeam(visitorTeam);

    // home team score
    int homeTeamScore = gameIndex + 1;
    game.setHomeFinalScore(homeTeamScore);

    // visitor team score, conditional based on homeTeamWins parameter
    int visitorTeamScore = homeTeamWins ? homeTeamScore - 1 : homeTeamScore + 1;
    game.setVisitorFinalScore(visitorTeamScore);

    // get each team's schedule.  we are assuming only one schedule here
    Schedule homeTeamSchedule = homeTeam.getSchedules().iterator().next();
    Schedule visitorTeamSchedule = visitorTeam.getSchedules().iterator().next();

    // this game belongs to each team's schedules
    homeTeamSchedule.addGame(game);
    visitorTeamSchedule.addGame(game);
  }


  @After
  public void tearDown() {
  }

  @Test
  public void testShouldGetLeagueStandings() {

    final String EXPECTED_SEASON_NAME = "Season-2010";

    // Act
    CalculateLeagueStandingsTask calculateLeagueStandingsTask = new CalculateLeagueStandingsTask(EXPECTED_SEASON_NAME);
    TaskExecutor.executeTask(calculateLeagueStandingsTask);

    // Assert
    assertEquals(EXPECTED_ACTIVE_TEAMS, calculateLeagueStandingsTask.getTeamStandings().size());

    // The first team has the best record 
    TeamStanding topTeamStanding = calculateLeagueStandingsTask.getTeamStandings().get(0);
    Team firstTeam = teams.get(0);
    assertEquals(firstTeam.getTeamId(), topTeamStanding.getTeamId());

    // The second team has the worse record
    TeamStanding lastTeamStanding = calculateLeagueStandingsTask.getTeamStandings().get(teams.size() - 1);
    Team secondTeam = teams.get(1);
    assertEquals(secondTeam.getTeamId(), lastTeamStanding.getTeamId());

  }
}
