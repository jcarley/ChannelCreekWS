package com.channelcreek.webservices.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 *
 * @author Jefferson Carley
 */
@Entity
public class Team implements Serializable {
  private Set<Schedule> schedules = new HashSet<Schedule>();
  private Set<Player> players = new HashSet<Player>();

  private long teamId;
  private String name;
  private boolean active;

  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  public long getTeamId() {
    return teamId;
  }

  protected void setTeamId(long teamId) {
    this.teamId = teamId;
  }

  @Column(length=50)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public boolean isActive() {
    return this.active;
  }

  @OneToMany(mappedBy="team", fetch=FetchType.LAZY)
  @Cascade(value={CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN})
  public Set<Schedule> getSchedules() {
    return schedules;
  }

  protected void setSchedules(Set<Schedule> schedules) {
    this.schedules = schedules;
  }

  public boolean addSchedule(Schedule schedule) {
    if(schedule != null && this.schedules.add(schedule)) {
      schedule.setTeam(this);
      return true;
    }

    return false;
  }

  public boolean removeSchedule(Schedule schedule) {
    if(schedule != null && this.schedules.remove(schedule)) {
      schedule.setTeam(null);
      return true;
    }

    return false;
  }

  @OneToMany(mappedBy = "team")
  @Cascade(value={CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN})
  public Set<Player> getPlayers() {
    return this.players;
  }

  protected void setPlayers(Set<Player> players) {
    this.players = players;
  }

  public boolean addPlayer(Player player) {

    if(player != null && this.players.add(player)) {
      player.setTeam(this);
      return true;
    }

    return false;
  }

  /**
   * This adds a list of players to this team.  This method is
   * an all or nothing operation.  If at least one player add
   * fails, then all players are rolled back.
   *
   * @param players the list of players to add
   * @return true if all players were added successfully, otherwise false
   */
  public boolean addPlayers(Player... players) {

    boolean isSuccess = true;

    for(Player player : players) {
      if(!addPlayer(player)) {
        isSuccess = false;
      }
    }

    if(!isSuccess) {
      for(Player player : players) {
        removePlayer(player);
      }
    }

    return isSuccess;
  }

  public boolean removePlayer(Player player) {
    if(player != null && this.players.remove(player)) {
      player.setTeam(null);
      return true;
    }

    return false;
  }

  @Transient
  public List<Player> getActivePlayers() {
    List<Player> activePlayers = new ArrayList<Player>();
    for(Player player : getPlayers()) {
      if(player.isActive())
        activePlayers.add(player);
    }
    return activePlayers;
  }
}
