package com.channelcreek.webservices.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 *
 * @author Jefferson Carley
 */
@Entity
public class Game implements Serializable {
  private long gameId;
  private String gameDate;
  private String location;
  private int homeFinalScore;
  private int visitorFinalScore;
  private Set<Schedule> schedules = new HashSet<Schedule>();

  @Id
  @GeneratedValue
  public long getGameId() {
    return gameId;
  }

  protected void setGameId(long gameId) {
    this.gameId = gameId;
  }

  public String getGameDate() {
    return gameDate;
  }

  public void setGameDate(String gameDate) {
    this.gameDate = gameDate;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public int getHomeFinalScore() {
    return homeFinalScore;
  }

  public void setHomeFinalScore(int homeFinalScore) {
    this.homeFinalScore = homeFinalScore;
  }

  public int getVisitorFinalScore() {
    return visitorFinalScore;
  }

  public void setVisitorFinalScore(int visitorFinalScore) {
    this.visitorFinalScore = visitorFinalScore;
  }

  @ManyToMany(mappedBy = "games")
  public Set<Schedule> getSchedules() {
    return schedules;
  }

  protected void setSchedules(Set<Schedule> schedules) {
    this.schedules = schedules;
  }

  public boolean addSchedule(Schedule schedule) {
    if(schedule != null && this.schedules.add(schedule)) {
      schedule.addGame(this);
      return true;
    }
    return false;
  }
}
