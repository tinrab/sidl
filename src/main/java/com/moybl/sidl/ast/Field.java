package com.moybl.sidl.ast;

import com.moybl.sidl.Position;

import java.util.List;

public class Field extends Node {

  private List<Attribute> attributes;
  private String name;
  private Type type;

  public Field(Position position, List<Attribute> attributes, String name, Type type) {
    super(position);
    this.attributes = attributes;
    this.name = name;
    this.type = type;
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  public List<Attribute> getAttributes() {
    return attributes;
  }

  public String getName() {
    return name;
  }

  public Type getType() {
    return type;
  }

}
