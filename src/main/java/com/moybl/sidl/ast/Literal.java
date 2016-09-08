package com.moybl.sidl.ast;

import com.moybl.sidl.Position;

public class Literal extends Node {

  public enum Kind {
    INTEGER,
    FLOAT,
    STRING
  }

  private Kind kind;
  private Object value;

  public Literal(Position position, Kind kind, Object value) {
    super(position);
    this.kind = kind;
    this.value = value;
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  public Kind getKind() {
    return kind;
  }

  public Object getValue() {
    return value;
  }

  public String getStringValue() {
    return (String) value;
  }

  public long getLongValue() {
    return (Long) value;
  }

  public double getDoubleValue() {
    return (Double) value;
  }

}
