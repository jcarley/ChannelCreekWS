package com.channelcreek.webservices.tests.data.builders;

import com.channelcreek.webservices.tests.data.mappers.TableMapper;

/**
 *
 * @author Jefferson Carley
 */
class DeleteBuilder implements Builder {

  private TableMapper table;

  DeleteBuilder(TableMapper table) {
    this.table = table;
  }

  @Override
  public String build() {
    StringBuilder sb = new StringBuilder();

    sb.append("delete from ");
    appendTableName(sb);

    return sb.toString();
  }

  private void appendTableName(StringBuilder sb) {
    sb.append(this.table.getName());
  }

}
