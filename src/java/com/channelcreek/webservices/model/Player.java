package com.channelcreek.webservices.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author Jefferson Carley
 */
@Entity
public class Player implements Serializable {
  private Team team;
  private long playerId;
  private String name;
  private boolean active;
  private int jerseyNumber;
  private String position;

  public Player() {
  }

  public Player(String name, boolean active, int jerseyNumber, String position) {
    this.name = name;
    this.active = active;
    this.jerseyNumber = jerseyNumber;
    this.position = position;
  }

  @Id
  @GeneratedValue
  public long getPlayerId() {
    return playerId;
  }

  protected void setPlayerId(long playerId) {
    this.playerId = playerId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public int getJerseyNumber() {
    return jerseyNumber;
  }

  public void setJerseyNumber(int jerseyNumber) {
    this.jerseyNumber = jerseyNumber;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
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
