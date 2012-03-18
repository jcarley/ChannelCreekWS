package com.channelcreek.webservices.tests.data.mappers;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;

/**
 *
 * @author Jefferson Carley
 */
public class TableMapper {

  public String tableName;
  public Set<FieldMapper> fields = new LinkedHashSet<FieldMapper>();

  public TableMapper(NodeTuple node) {
    compose(node);
  }

  public String getName() {
    return this.tableName;
  }

  public Set<FieldMapper> getFields() {
    return this.fields;
  }

  /**
   * Builds a ruby style hash of the fields for this table.  Mostly
   * used for debugging purposes.
   *
   * @return A string of a ruby style hash.
   */
  public String getColumnMap() {
    StringBuilder b = new StringBuilder();

    if(fields.isEmpty())
      return b.toString();

    b.append("{");

    boolean isFirst = true;

    for(FieldMapper column : this.fields) {
      String name = column.getName();
      Object value = column.getValue();

      if(!isFirst) {
        b.append(", ");
      }

      b.append(name).append(": ").append(value);

      if(isFirst) {
        isFirst = false;
      }
    }

    b.append("}");

    return b.toString();
  }

  private void compose(NodeTuple node) {

    ScalarNode key = (ScalarNode)node.getKeyNode();
    this.tableName = key.getValue();

    MappingNode columnNodes = (MappingNode)node.getValueNode();

    Iterator<NodeTuple> iterator = columnNodes.getValue().iterator();

    while(iterator.hasNext()) {
      NodeTuple columnNode = iterator.next();

      this.fields.add(new FieldMapper(columnNode));
    }
  }

}
