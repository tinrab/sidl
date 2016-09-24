package com.moybl.sidl.ast;

import com.moybl.sidl.Position;

import java.util.List;

public class StructDefinition extends Definition {

  private Identifier name;
  private List<Field> fields;

  public StructDefinition(Position position, Identifier name, List<Field> fields) {
    super(position);
    this.name = name;
    this.fields = fields;
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  public Identifier getName() {
    return name;
  }

  public String getDefinedName() {
    return name.getCanonicalName();
  }

  public List<Field> getFields() {
    return fields;
  }

}
