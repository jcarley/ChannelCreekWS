package com.channelcreek.webservices.tasks;

import com.channelcreek.infrastructure.tasks.BaseTask;
import com.channelcreek.webservices.model.Schedule;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Jefferson Carley
 */
public class RetrieveTeamScheduleTask extends BaseTask {

  private long teamId;
  private String seasonName;
  private Schedule schedule;

  public RetrieveTeamScheduleTask(long teamId, String seasonName) {
    this.teamId = teamId;
    this.seasonName = seasonName;
  }

  public Schedule getSchedule() {
    return this.schedule;
  }

  @Override
  public void Execute() throws Exception {

    // this will get us the schedule and eager fetch the schedule's games
    // the games collection is normally lazy loaded
    String queryString =
            "from Schedule schedule join fetch schedule.games " +
            "where schedule.team.teamId = :teamId and schedule.seasonName = :seasonName";

    Session session = super.getSession();
    Query query = session.createQuery(queryString);
    query.setLong("teamId", this.teamId);
    query.setString("seasonName", this.seasonName);
    this.schedule = (Schedule)query.uniqueResult();
  }

}
