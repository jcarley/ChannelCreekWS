package com.channelcreek.webservices.tasks;

import com.channelcreek.infrastructure.tasks.BaseTask;
import com.channelcreek.webservices.model.Game;
import com.channelcreek.webservices.model.Schedule;
import com.channelcreek.webservices.model.Team;
import com.channelcreek.webservices.model.TeamStanding;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Jefferson Carley
 */
public class CalculateLeagueStandingsTask extends BaseTask {

  private String seasonName;
  private List<TeamStanding> teamStandings;

  public CalculateLeagueStandingsTask(String seasonName) {
    this.seasonName = seasonName;
  }

  @Override
  public void Execute() throws Exception {

    // query for all schedules with the matching season name
    String queryString =
            "from Schedule schedule join fetch schedule.games " +
            "where schedule.seasonName = :seasonName";

    Session session = super.getSession();
    Query query = session.createQuery(queryString);
    query.setString("seasonName", this.seasonName);
    List queryResults = query.list();

    // The query returns all the schedule records that match the seasonName.  This will
    // result in duplicate games.  We have to get a distinct list of games.
    Set<Game> games = getUniqueGames(queryResults);

    // With the unique list of games, we can calculate the standings for this season
    calculateTeamStandings(games);

    sort();
  }

  public List<TeamStanding> getTeamStandings() {
    return teamStandings;
  }

  /*
   * Calculates team standings from a list of distinct games.
   */
  private void calculateTeamStandings(Set<Game> games) {

    // a hashmap to track distinct team standings records
    Map<Long, TeamStanding> teamStandingsMap = new HashMap<Long, TeamStanding>();

    for(Game game : games) {

      // if the game hasn't been played, then it doesn't count towards the standings
      if(!hasGameBeenPlayed(game)) {
        continue;
      }

      // this unfortunatley is an n+1 scenerio, but its a trade off because some games
      // haven't been played, so pull team info is not necessary
      Team homeTeam = game.getHomeTeam();
      Team visitorTeam = game.getVisitorTeam();

      // get the team scores
      int homeTeamScore = game.getHomeFinalScore();
      int visitorTeamScore = game.getVisitorFinalScore();

      // get each team standing record
      TeamStanding homeTeamStanding = getTeamStanding(homeTeam, teamStandingsMap);
      TeamStanding visitorTeamStanding = getTeamStanding(visitorTeam, teamStandingsMap);

      // who won
      if(homeTeamScore > visitorTeamScore) {
        homeTeamStanding.addToTotalWins();
        visitorTeamStanding.addToTotalLoses();
      } else {
        homeTeamStanding.addToTotalLoses();
        visitorTeamStanding.addToTotalWins();
      }
    }

    // we only return the team standings.  we are done with the team standings hashmap
    this.teamStandings = new LinkedList<TeamStanding>(teamStandingsMap.values());
  }

  /*
   * Gets a team standing record.  If the team standing hasn't been used yet, it is created
   * then returned.
   */
  private TeamStanding getTeamStanding(Team team, Map<Long, TeamStanding> teamStandingsMap) {
    TeamStanding teamStanding = teamStandingsMap.get(team.getTeamId());

    if(teamStanding == null) {
      teamStanding = new TeamStanding(team);
      teamStandingsMap.put(team.getTeamId(), teamStanding);
    }

    return teamStanding;
  }

  /*
   * Sorts the team standings according to games won and lossed.  Does not take into account teams
   * with the same record
   */
  private void sort() {
    Collections.sort(this.teamStandings);
  }

  /*
   * Gets a list of unique games from the list of schedules
   */
  private Set<Game> getUniqueGames(List schedulesList) {

    Schedule schedule;
    Set<Game> games = new HashSet<Game>();
    Iterator schedulesIterator = schedulesList.iterator();

    while(schedulesIterator.hasNext()) {
      schedule = (Schedule)schedulesIterator.next();

      for(Game game : schedule.getGames()) {
        if(!games.contains(game)) {
          games.add(game);
        }
      }
    }

    return games;
  }

  /*
   * Helper method to determine if a game has been played yet.
   */
  private boolean hasGameBeenPlayed(Game game) {
    String gameDateString = game.getGameDate();

    Date gameDate;
    try {

      SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
      gameDate = formatter.parse(gameDateString);

    } catch (ParseException ex) {
      return false;
    }

    Calendar calendar = Calendar.getInstance();
    Date today = calendar.getTime();

    return today.after(gameDate);
  }

}
