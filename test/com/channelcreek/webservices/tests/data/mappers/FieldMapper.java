package com.channelcreek.webservices.tests.data.mappers;

import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;

/**
 *
 * @author Jefferson Carley
 */
public class FieldMapper {

  private String name;
  private String value;
  private String fileType;

  FieldMapper(NodeTuple columnNode) {
    compose(columnNode);
  }

  private void compose(NodeTuple columnNode) {

    ScalarNode columnNameNode = (ScalarNode) columnNode.getKeyNode();
    ScalarNode columnValueNode = (ScalarNode) columnNode.getValueNode();

    this.name = columnNameNode.getValue();
    this.value = columnValueNode.getValue();
    this.fileType = columnValueNode.getTag().getClassName();
  }

  public String getName() {
    return name;
  }

  public Object getValue() {
    return value;
  }

  public String getEscapedValue() {
    if(getFileType().equals("str"))
      return "'" + getValue().toString() + "'";
    else if(getFileType().equals("bool"))
      return getValue().toString().equals("true") ? "1" : "0";
    else
      return getValue().toString();
  }

  public String getFileType() {
    return fileType;
  }
}
