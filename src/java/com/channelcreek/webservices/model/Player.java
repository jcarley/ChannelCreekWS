package com.channelcreek.webservices.model;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.ForeignKey;

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
  @GeneratedValue(strategy= GenerationType.AUTO)
  public long getPlayerId() {
    return playerId;
  }

  protected void setPlayerId(long playerId) {
    this.playerId = playerId;
  }

  @Column(length=50)
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

  @Column(length=25)
  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  @ManyToOne(optional=false)
  @JoinColumn(name="team_id")
  @ForeignKey(name="fk_player_team")
  public Team getTeam() {
    return team;
  }

  public void setTeam(Team team) {
    this.team = team;

//    if(team != null) {
//      team.addPlayer(this);
//    } else {
//      team.removePlayer(this);
//    }
  }

}
