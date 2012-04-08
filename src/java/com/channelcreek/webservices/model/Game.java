package com.channelcreek.webservices.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.ForeignKey;

/**
 *
 * @author Jefferson Carley
 */
@Entity
public class Game implements Serializable {
  private long gameId;
  private String gameDate;
  private String location;
  private Team homeTeam;
  private Team visitorTeam;
  private int homeFinalScore;
  private int visitorFinalScore;
  private Set<Schedule> schedules = new HashSet<Schedule>();

  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
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

  @Column(length=75)
  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  @Column(nullable=true)
  public int getHomeFinalScore() {
    return homeFinalScore;
  }

  public void setHomeFinalScore(int homeFinalScore) {
    this.homeFinalScore = homeFinalScore;
  }

  @Column(nullable=true)
  public int getVisitorFinalScore() {
    return visitorFinalScore;
  }

  public void setVisitorFinalScore(int visitorFinalScore) {
    this.visitorFinalScore = visitorFinalScore;
  }

  @OneToOne(optional=true)
  @ForeignKey(name="fk_home_team")
  public Team getHomeTeam() {
    return homeTeam;
  }

  public void setHomeTeam(Team homeTeam) {
    this.homeTeam = homeTeam;
  }

  @OneToOne(optional=true)
  @ForeignKey(name="fk_visitor_team")
  public Team getVisitorTeam() {
    return visitorTeam;
  }

  public void setVisitorTeam(Team visitorTeam) {
    this.visitorTeam = visitorTeam;
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
