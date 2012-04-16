package com.channelcreek.webservices.model;

/**
 *
 * @author Jefferson Carley
 */
public class TeamStanding implements Comparable {
  private long teamId;
  private String teamName;
  private int totalWins;
  private int totalLoses;
  private int rank;

  public TeamStanding(Team team) {
    this.teamId = team.getTeamId();
    this.teamName = team.getName();
  }

  public long getTeamId() {
    return teamId;
  }

  public void setTeamId(long teamId) {
    this.teamId = teamId;
  }

  public String getTeamName() {
    return teamName;
  }

  public void setTeamName(String teamName) {
    this.teamName = teamName;
  }

  public int getTotalWins() {
    return totalWins;
  }

  public void setTotalWins(int totalWins) {
    this.totalWins = totalWins;
  }

  public int getTotalLoses() {
    return totalLoses;
  }

  public void setTotalLoses(int totalLoses) {
    this.totalLoses = totalLoses;
  }

  public int getRank() {
    return rank;
  }

  public void setRank(int rank) {
    this.rank = rank;
  }

  public void addToTotalWins() {
    this.totalWins++;
  }

  public void addToTotalLoses() {
    this.totalLoses++;
  }

  @Override
  public int compareTo(Object t) {
    TeamStanding other = (TeamStanding)t;

    if(this.totalWins == other.totalWins && this.totalLoses == other.totalLoses)
      return 0;

    if(this.totalWins > other.totalWins)
      return -1;

    return 1;
  }
}
