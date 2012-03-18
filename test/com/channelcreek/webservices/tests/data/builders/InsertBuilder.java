package com.channelcreek.webservices.tests.data.builders;

import com.channelcreek.webservices.tests.data.mappers.FieldMapper;
import com.channelcreek.webservices.tests.data.mappers.TableMapper;

/**
 *
 * @author Jefferson Carley
 */
class InsertBuilder implements Builder {

  private TableMapper table;

  InsertBuilder(TableMapper table) {
    this.table = table;
  }

  @Override
  public String build() {
    StringBuilder sb = new StringBuilder();

    sb.append("insert into ");
    appendTableName(sb);
    sb.append("(");
    appendFieldNames(sb);
    sb.append(") ");
    sb.append("Values (");
    appendFieldValues(sb);
    sb.append(")");

    return sb.toString();
  }

  private void appendTableName(StringBuilder sb) {
    sb.append(this.table.getName());
  }

  private void appendFieldNames(StringBuilder sb) {
    boolean isFirst = true;

    for(FieldMapper field : this.table.getFields()) {

      if(!isFirst) {
        sb.append(", ");
      }

      sb.append(field.getName());

      if(isFirst) {
        isFirst = false;
      }
    }
  }

  private void appendFieldValues(StringBuilder sb) {
    boolean isFirst = true;

    for(FieldMapper field : this.table.getFields()) {

      if(!isFirst) {
        sb.append(", ");
      }

      sb.append(field.getEscapedValue());

      if(isFirst) {
        isFirst = false;
      }
    }
  }

}
