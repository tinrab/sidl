package com.moybl.sidl.ast;

import com.moybl.sidl.Position;
import com.moybl.sidl.Token;

import java.util.List;

public class EnumDefinition extends Definition {

  private Identifier name;
  private List<EnumValue> values;
  private Token type;

  public EnumDefinition(Position position, Identifier name, Token type, List<EnumValue> values) {
    super(position);
    this.name = name;
    this.type = type;
    this.values = values;
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

  public Token getType() {
    return type;
  }

  public List<EnumValue> getValues() {
    return values;
  }

}
