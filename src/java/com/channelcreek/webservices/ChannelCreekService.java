package com.channelcreek.webservices;

import com.channelcreek.infrastructure.tasks.TaskExecutor;
import com.channelcreek.webservices.model.Game;
import com.channelcreek.webservices.model.Player;
import com.channelcreek.webservices.model.Team;
import com.channelcreek.webservices.model.TeamStanding;
import com.channelcreek.webservices.tasks.*;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 *
 * @author Jefferson Carley
 */
@WebService(serviceName = "ChannelCreekService")
public class ChannelCreekService {

  @WebMethod(operationName = "calculateTeamStandings")
  public List<TeamStanding> calculateTeamStandings(String seasonName) {

    CalculateLeagueStandingsTask calculateLeagueStandingsTask = new CalculateLeagueStandingsTask(seasonName);
    TaskExecutor.executeTask(calculateLeagueStandingsTask);
    return calculateLeagueStandingsTask.getTeamStandings();
  }

  @WebMethod(operationName = "retrieveTeamSchedule")
  public List<Game> retrieveTeamSchedule(int teamId, String seasonName) {
    RetrieveTeamScheduleTask retrieveTeamScheduleTask = new RetrieveTeamScheduleTask(teamId, seasonName);
    TaskExecutor.executeTask(retrieveTeamScheduleTask);
    return (List<Game>) retrieveTeamScheduleTask.getSchedule().getGames();
  }

  @WebMethod(operationName = "reportGameScores")
  public Game reportGameScores(int gameId) {
    ReportGameScoresTask reportGameScoresTask = new ReportGameScoresTask(gameId);
    TaskExecutor.executeTask(reportGameScoresTask);
    return reportGameScoresTask.getGame();
  }

  @WebMethod(operationName = "retrieveAllActiveTeams")
  public List<Team> retrieveAllActiveTeams() {
    FindActiveTeamsTask findActiveTeamsTask = new FindActiveTeamsTask();
    TaskExecutor.executeTask(findActiveTeamsTask);
    return findActiveTeamsTask.getActiveTeams();
  }

  @WebMethod(operationName = "retrieveAllActivePlayers")
  public List<Player> retrieveAllActivePlayers(int teamId) {
    FindActivePlayersTask findActivePlayersTask = new FindActivePlayersTask(teamId);
    TaskExecutor.executeTask(findActivePlayersTask);
    return findActivePlayersTask.getPlayers();
  }

  @WebMethod(operationName = "submitPlayerToTeamRoster")
  public Player submitPlayerToTeamRoster(int teamId, String name, int jerseyNumber, String position) {

    SubmitPlayerToTeamRosterTask submitPlayerToTeamRosterTask = new SubmitPlayerToTeamRosterTask(
            teamId,
            name,
            jerseyNumber,
            position,
            true);
    TaskExecutor.executeTask(submitPlayerToTeamRosterTask);
    return submitPlayerToTeamRosterTask.getPlayer();
  }

  @WebMethod(operationName = "activatePlayer")
  public Player activatePlayer(int teamId, int playerId) {
    ActivatePlayerTask activatePlayerTask = new ActivatePlayerTask(teamId, playerId);
    TaskExecutor.executeTask(activatePlayerTask);
    return activatePlayerTask.getPlayer();
  }
  
}
