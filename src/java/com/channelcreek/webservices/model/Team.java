package com.channelcreek.webservices.model;

import java.io.Serializable;
import java.util.HashSet;
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
  private Set<Schedule> schedules;
  private Set<Player> players;

  private long teamId;
  private String name;
  private boolean active;

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE)
  protected long getTeamId() {
    return teamId;
  }

  protected void setTeamId(long teamId) {
    this.teamId = teamId;
  }

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

  @OneToMany(mappedBy="team")
  public Set<Schedule> getSchedules() {
    return schedules;
  }

  public Schedule getSchedule(String seasonName) {
    return null;
  }

  public void setSchedules(Set<Schedule> schedules) {
    this.schedules = schedules;
  }

  @OneToMany(mappedBy = "team")
  @Cascade(value={CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN})
  public Set<Player> getPlayers() {
    return this.players;
  }

  protected void setPlayers(Set<Player> players) {
    this.players = players;
  }

  @Transient
  public Set<Player> getActivePlayers() {
    Set<Player> activePlayers = new HashSet<Player>();
    for(Player player : getPlayers()) {
      if(player.isActive())
        activePlayers.add(player);
    }
    return activePlayers;
  }
}
