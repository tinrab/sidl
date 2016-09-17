package com.moybl.sidl.ast;

import com.moybl.sidl.Position;

import java.util.List;

public class Function extends Node {

  private String name;
  private List<Parameter> parameters;
  private Type type;

  public Function(Position position, String name, List<Parameter> parameters, Type type) {
    super(position);
    this.name = name;
    this.parameters = parameters;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public List<Parameter> getParameters() {
    return parameters;
  }

  public Type getType() {
    return type;
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

}
