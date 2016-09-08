package com.moybl.sidl.semantics;

import com.moybl.sidl.Position;

public class SemanticException extends RuntimeException {

  private Position position;

  public SemanticException(String message) {
    super(message);
  }

  public SemanticException(Position position, String message) {
    super(position.toString() + ": " + message);

    this.position = position;
  }

  public Position getPosition() {
    return position;
  }

  public static SemanticException internal() {
    return new SemanticException("Internal error");
  }

  public static SemanticException illegalInsert() {
    return new SemanticException("Illegal insert");
  }

  public static SemanticException illegalRemove() {
    return new SemanticException("Illegal remove");
  }

  public static SemanticException undefined(String name) {
    return new SemanticException("Undefined '" + name + "'");
  }

  public static SemanticException illegalInterfaceParent(Position position) {
    return new SemanticException(position, "Illegal interface parent");
  }

}
