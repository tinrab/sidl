package com.moybl.sidl.ast;

import com.moybl.sidl.Position;

import java.util.List;
import java.util.Map;

public abstract class Definition extends Node {

  private Map<String, Attribute> attributes;

  public Definition(Position position) {
    super(position);
  }

  public abstract Identifier getName();

  public abstract String getDefinedName();

  public Map<String, Attribute> getAttributes() {
    return attributes;
  }

  public void setAttributes(Map<String, Attribute> attributes) {
    this.attributes = attributes;
  }

}
