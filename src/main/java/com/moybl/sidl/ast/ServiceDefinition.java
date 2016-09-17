package com.moybl.sidl.ast;

import com.moybl.sidl.Position;

import java.util.List;

public class ServiceDefinition extends Definition {

  private Identifier name;
  private Identifier parent;
  private ServiceDefinition parentDefinition;
  private List<Function> functions;

  public ServiceDefinition(Position position, Identifier name, Identifier parent, List<Function> functions) {
    super(position);
    this.name = name;
    this.functions = functions;
    this.parent = parent;
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

  public List<Function> getFunctions() {
    return functions;
  }

  public Identifier getParent() {
    return parent;
  }

  public ServiceDefinition getParentDefinition() {
    return parentDefinition;
  }

  public void setParentDefinition(ServiceDefinition parentDefinition) {
    this.parentDefinition = parentDefinition;
  }

}
