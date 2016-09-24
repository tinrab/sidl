package com.moybl.sidl.ast;

import com.moybl.sidl.Position;

import java.util.ArrayList;
import java.util.List;

public class ClassDefinition extends Definition {

  private List<ClassDefinition> children;
  private Identifier name;
  private Identifier parent;
  private Definition parentDefinition;
  private List<Definition> parentPath;
  private List<Field> fields;

  public ClassDefinition(Position position, Identifier name, Identifier parent, List<Field> fields) {
    super(position);
    this.name = name;
    this.parent = parent;
    this.fields = fields;
    children = new ArrayList<ClassDefinition>();
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  public List<ClassDefinition> getChildren() {
    return children;
  }

  public void setChildren(List<ClassDefinition> children) {
    this.children = children;
  }

  public Identifier getName() {
    return name;
  }

  public Identifier getParent() {
    return parent;
  }

  public Definition getParentDefinition() {
    return parentDefinition;
  }

  public void setParentDefinition(Definition parentDefinition) {
    this.parentDefinition = parentDefinition;

    parentPath = new ArrayList<Definition>();

    do {
      parentPath.add(parentDefinition);

      if (parentDefinition instanceof ClassDefinition) {
        ClassDefinition ptd = (ClassDefinition) parentDefinition;
        if (ptd.getParentDefinition() != null) {
          parentDefinition = ptd.getParentDefinition();
        } else {
          break;
        }
      } else if (parentDefinition instanceof InterfaceDefinition) {
        InterfaceDefinition pid = (InterfaceDefinition) parentDefinition;
        if (pid.getParentDefinition() != null) {
          parentDefinition = pid.getParentDefinition();
        } else {
          break;
        }
      } else {
        break;
      }
    } while (parentDefinition != null);
  }

  public List<Definition> getParentPath() {
    return parentPath;
  }

  public String getDefinedName() {
    return name.getCanonicalName();
  }

  public List<Field> getFields() {
    return fields;
  }

}
