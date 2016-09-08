package com.moybl.sidl.ast;

import com.moybl.sidl.Position;

public class AttributeEntry extends Node {

  private String name;
  private Literal value;

  public AttributeEntry(Position position, String name, Literal value) {
    super(position);
    this.name = name;
    this.value = value;
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  public String getName() {
    return name;
  }

  public Literal getValue() {
    return value;
  }

}
