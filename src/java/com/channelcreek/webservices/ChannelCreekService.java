package com.channelcreek.webservices;

import com.channelcreek.infrastructure.tasks.TaskExecutor;
import com.channelcreek.webservices.model.Game;
import com.channelcreek.webservices.model.Player;
import com.channelcreek.webservices.model.Team;
import com.channelcreek.webservices.model.TeamStanding;
import com.channelcreek.webservices.tasks.*;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *
 * @author Jefferson Carley
 */
@WebService(serviceName = "ChannelCreekService")
public class ChannelCreekService {

  @WebMethod(operationName = "calculateTeamStandings")
  public List<TeamStanding> calculateTeamStandings(@WebParam(name = "seasonName") String seasonName) {

    CalculateLeagueStandingsTask calculateLeagueStandingsTask = new CalculateLeagueStandingsTask(seasonName);
    TaskExecutor.executeTask(calculateLeagueStandingsTask);
    return calculateLeagueStandingsTask.getTeamStandings();
  }

  @WebMethod(operationName = "retrieveTeamSchedule")
  public List<Game> retrieveTeamSchedule(@WebParam(name = "teamId") int teamId, @WebParam(name = "seasonName") String seasonName) {
    RetrieveTeamScheduleTask retrieveTeamScheduleTask = new RetrieveTeamScheduleTask(teamId, seasonName);
    TaskExecutor.executeTask(retrieveTeamScheduleTask);
    return (List<Game>) retrieveTeamScheduleTask.getSchedule().getGames();
  }

  @WebMethod(operationName = "reportGameScores")
  public Game reportGameScores(@WebParam(name = "gameId") int gameId) {
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
  public List<Player> retrieveAllActivePlayers(@WebParam(name = "teamId") int teamId) {
    FindActivePlayersTask findActivePlayersTask = new FindActivePlayersTask(teamId);
    TaskExecutor.executeTask(findActivePlayersTask);
    return findActivePlayersTask.getPlayers();
  }

  @WebMethod(operationName = "submitPlayerToTeamRoster")
  public Player submitPlayerToTeamRoster(@WebParam(name = "teamId") int teamId, @WebParam(name = "name") String name, @WebParam(name = "jerseyNumber") int jerseyNumber, @WebParam(name = "position") String position) {

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
  public Player activatePlayer(@WebParam(name = "teamId") int teamId, @WebParam(name = "playerId") int playerId) {
    ActivatePlayerTask activatePlayerTask = new ActivatePlayerTask(teamId, playerId);
    TaskExecutor.executeTask(activatePlayerTask);
    return activatePlayerTask.getPlayer();
  }

}
