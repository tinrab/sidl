package com.moybl.sidl.ast;

import com.moybl.sidl.Position;

import java.util.ArrayList;
import java.util.List;

public class TypeDefinition extends Definition {

  private List<TypeDefinition> children;
  private Identifier name;
  private Identifier parent;
  private Definition parentDefinition;
  private List<Definition> parentPath;
  private Identifier oldName;
  private Type type;
  private List<Field> fields;

  public TypeDefinition(Position position, Identifier name, Identifier oldName) {
    super(position);
    this.name = name;
    this.oldName = oldName;
    children = new ArrayList<TypeDefinition>();
  }

  public TypeDefinition(Position position, Identifier name, Type type) {
    super(position);
    this.name = name;
    this.type = type;
    children = new ArrayList<TypeDefinition>();
  }

  public TypeDefinition(Position position, Identifier name, Identifier parent, List<Field> fields) {
    super(position);
    this.name = name;
    this.parent = parent;
    this.fields = fields;
    children = new ArrayList<TypeDefinition>();
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  public boolean hasChildren() {
    return children.size() != 0;
  }

  public List<TypeDefinition> getChildren() {
    return children;
  }

  public void setChildren(List<TypeDefinition> children) {
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

      if (parentDefinition instanceof TypeDefinition) {
        TypeDefinition ptd = (TypeDefinition) parentDefinition;
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

  public Identifier getOldName() {
    return oldName;
  }

  public Type getType() {
    return type;
  }

  public List<Field> getFields() {
    return fields;
  }

}
