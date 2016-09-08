package com.moybl.sidl.ast;

import com.moybl.sidl.Position;
import com.moybl.sidl.Token;

public class PrimaryType extends Type {

  private Identifier name;
  private Token token;
  private boolean isReference;
  private Definition definition;

  public PrimaryType(Position position, Identifier name, boolean isReference) {
    super(position);
    this.name = name;
    this.isReference = isReference;
    token = Token.IDENTIFIER;
  }

  public PrimaryType(Position position, Token token, boolean isReference) {
    super(position);
    this.token = token;
    this.isReference = isReference;
  }

  public Identifier getName() {
    return name;
  }

  public Token getToken() {
    return token;
  }

  public boolean isReference() {
    return isReference;
  }

  public Definition getDefinition() {
    return definition;
  }

  public void setDefinition(Definition definition) {
    this.definition = definition;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

}
