package com.channelcreek.webservices.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

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
  private List<Schedule> schedules;

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE)
  protected long getGameId() {
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
  public List<Schedule> getSchedules() {
    return schedules;
  }

  public void setSchedules(List<Schedule> schedules) {
    this.schedules = schedules;
  }
}
