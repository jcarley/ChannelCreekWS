package com.channelcreek.webservices.model;

/**
 *
 * @author Jefferson Carley
 */
public class TeamStanding {
  private long teamId;
  private String teamName;
  private int totalWins;
  private int totalLoses;
  private int rank;

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
}
