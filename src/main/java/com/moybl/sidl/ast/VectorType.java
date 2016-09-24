package com.moybl.sidl.ast;

import com.moybl.sidl.Position;

public class VectorType extends Type {

  private PrimaryType type;

  public VectorType(Position position, PrimaryType type) {
    super(position);
    this.type = type;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  public PrimaryType getType() {
    return type;
  }

}
