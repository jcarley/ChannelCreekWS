package com.channelcreek.webservices.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.ForeignKey;

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
  @GeneratedValue(strategy= GenerationType.AUTO)
  protected long getScheduleId() {
    return scheduleId;
  }

  protected void setScheduleId(long scheduleId) {
    this.scheduleId = scheduleId;
  }

  @Column(length=25)
  public String getSeasonName() {
    return seasonName;
  }

  public void setSeasonName(String seasonName) {
    this.seasonName = seasonName;
  }

  @ManyToMany(fetch=FetchType.LAZY)
  @JoinTable(
          joinColumns={@JoinColumn(name="schedule_id")},
          inverseJoinColumns={@JoinColumn(name="game_id")})
  @Cascade(value={CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN})
  @ForeignKey(name="fk_schedule_games", inverseName="fk_game_schedules")
  public Set<Game> getGames() {
    return games;
  }

  protected void setGames(Set<Game> games) {
    this.games = games;
  }

  public boolean addGame(Game game) {

    if(game != null && this.games.add(game)) {
      game.addSchedule(this);
      return true;
    }

    return false;
  }

  @ManyToOne(optional=false, fetch= FetchType.LAZY)
  @JoinColumn(name="team_id")
  @ForeignKey(name="fk_schedule_team")
  public Team getTeam() {
    return team;
  }

  public void setTeam(Team team) {
    this.team = team;
  }
}
