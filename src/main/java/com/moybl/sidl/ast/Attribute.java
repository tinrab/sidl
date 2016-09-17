package com.moybl.sidl.ast;

import com.moybl.sidl.Position;

import java.util.List;
import java.util.Map;

public class Attribute extends Node {

  private String name;
  private Map<String, AttributeEntry> entries;

  public Attribute(Position position, String name, Map<String, AttributeEntry> entries) {
    super(position);
    this.name = name;
    this.entries = entries;
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  public String getName() {
    return name;
  }

  public Map<String, AttributeEntry> getEntries() {
    return entries;
  }

}
