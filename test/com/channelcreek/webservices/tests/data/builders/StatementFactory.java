package com.channelcreek.webservices.tests.data.builders;

import com.channelcreek.webservices.tests.data.mappers.TableMapper;

/**
 *
 * @author Jefferson Carley
 */
public class StatementFactory {

  public String Insert(TableMapper table) {
    Builder builder = new InsertBuilder(table);
    return builder.build();
  }

  public String Delete(TableMapper table) {
    Builder builder = new DeleteBuilder(table);
    return builder.build();
  }

}
