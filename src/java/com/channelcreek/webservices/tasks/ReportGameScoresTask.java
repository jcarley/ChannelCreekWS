package com.channelcreek.webservices.tasks;

import com.channelcreek.infrastructure.tasks.BaseTask;
import com.channelcreek.webservices.model.Game;
import org.hibernate.Session;

/**
 *
 * @author Jefferson Carley
 */
public class ReportGameScoresTask extends BaseTask {

  private final long gameId;
  private Game game;

  public ReportGameScoresTask(long gameId) {
    this.gameId = gameId;
  }

  @Override
  public void Execute() throws Exception {
    Session session = super.getSession();
    this.game = (Game)session.get(Game.class, this.gameId);
  }

  public Game getGame() {
    return this.game;
  }

}
