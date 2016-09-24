package com.moybl.sidl.ast;

import com.moybl.sidl.Position;
import com.moybl.sidl.Token;

public class PrimaryType extends Type {

  private Identifier name;
  private Token token;
  private Definition definition;

  public PrimaryType(Position position, Identifier name) {
    super(position);
    this.name = name;
    token = Token.IDENTIFIER;
  }

  public PrimaryType(Position position, Token token) {
    super(position);
    this.token = token;
  }

  public Identifier getName() {
    return name;
  }

  public Token getToken() {
    return token;
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
