package com.moybl.sidl.ast;

import com.moybl.sidl.Position;

import java.util.List;

public class Document extends Node {

  private List<Definition> definitions;

  public Document(Position position, List<Definition> definitions) {
    super(position);
    this.definitions = definitions;
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  public List<Definition> getDefinitions() {
    return definitions;
  }

}
