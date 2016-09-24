package com.moybl.sidl.ast;

import com.moybl.sidl.Position;

public class MapType extends Type {

  private PrimaryType keyType;
  private PrimaryType valueType;

  public MapType(Position position, PrimaryType keyTpe, PrimaryType valueType) {
    super(position);
    this.keyType = keyTpe;
    this.valueType = valueType;
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  public PrimaryType getKeyType() {
    return keyType;
  }

  public PrimaryType getValueType() {
    return valueType;
  }

}
