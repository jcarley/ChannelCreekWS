package com.channelcreek.webservices.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 *
 * @author Jefferson Carley
 */
@Entity
public class Schedule implements Serializable {
  private long scheduleId;
  private String seasonName;
  private Team team;
  private Set<Game> games = new HashSet<Game>();

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE)
  protected long getScheduleId() {
    return scheduleId;
  }

  protected void setScheduleId(long scheduleId) {
    this.scheduleId = scheduleId;
  }

  public String getSeasonName() {
    return seasonName;
  }

  public void setSeasonName(String seasonName) {
    this.seasonName = seasonName;
  }

  @ManyToMany
  @JoinTable(
          joinColumns={@JoinColumn(name="schedule_id")},
          inverseJoinColumns={@JoinColumn(name="game_id")})
  public Set<Game> getGames() {
    return games;
  }

  void setGames(Set<Game> games) {
    this.games = games;
  }

  public void addGame(Game game) {
    getGames().add(game);
  }

  @ManyToOne
  @JoinColumn(name="team_id")
  public Team getTeam() {
    return team;
  }

  public void setTeam(Team team) {
    this.team = team;
  }
}
