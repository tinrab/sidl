package com.moybl.sidl.ast;

import com.moybl.sidl.Position;

public class EnumValue extends Node {

  private String name;
  private Literal value;

  public EnumValue(Position position, String name, Literal value) {
    super(position);
    this.name = name;
    this.value = value;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  public String getName() {
    return name;
  }

  public Literal getValue() {
    return value;
  }

  public void setValue(Literal value) {
    this.value = value;
  }

}
