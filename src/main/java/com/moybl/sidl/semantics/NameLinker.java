package com.moybl.sidl.semantics;

import com.moybl.sidl.ParserException;
import com.moybl.sidl.ast.*;

import java.util.HashMap;
import java.util.Map;

public class NameLinker implements Visitor {

  private Map<String, Definition> typeNames;

  public NameLinker() {
    typeNames = new HashMap<String, Definition>();
  }

  public void visit(Document node) {
    for (int i = 0; i < node.getDefinitions().size(); i++) {
      Definition d = node.getDefinitions().get(i);

      if (d instanceof TypeDefinition || d instanceof EnumDefinition || d instanceof InterfaceDefinition) {
        typeNames.put(d.getDefinedName(), d);
      }
    }

    for (int i = 0; i < node.getDefinitions().size(); i++) {
      node.getDefinitions().get(i).accept(this);
    }
  }

  public void visit(TypeDefinition node) {
    if (node.getOldName() != null) {
      node.getOldName().accept(this);
    } else {
      if (node.getParent() != null) {
        String parent = node.getParent().getCanonicalName();

        if (!typeNames.containsKey(parent)) {
          throw ParserException.undefined(node.getPosition(), parent);
        } else {
          Definition pd = typeNames.get(parent);

          if (pd instanceof TypeDefinition) {
            ((TypeDefinition) pd).getChildren().add(node);
          } else if (pd instanceof InterfaceDefinition) {
            ((InterfaceDefinition) pd).getChildren().add(node);
          }

          node.setParentDefinition(pd);
        }
      }

      for (int i = 0; i < node.getFields().size(); i++) {
        node.getFields().get(i).accept(this);
      }
    }
  }

  public void visit(EnumDefinition node) {
  }

  public void visit(Field node) {
    node.getType().accept(this);
  }

  public void visit(ArrayType node) {
    node.getType().accept(this);
  }

  public void visit(ListType node) {
    node.getType().accept(this);
  }

  public void visit(Identifier node) {
    if (!typeNames.containsKey(node.getCanonicalName())) {
      throw ParserException.undefined(node.getPosition(), node.getCanonicalName());
    }
  }

  public void visit(EnumValue node) {
  }

  public void visit(PrimaryType node) {
    if (node.getName() != null) {
      node.getName().accept(this);
      node.setDefinition(typeNames.get(node.getName().getCanonicalName()));
    }
  }

  public void visit(NamespaceDefinition node) {
  }

  public void visit(Attribute node) {
  }

  public void visit(AttributeEntry node) {
  }

  public void visit(Literal node) {
  }

  public void visit(InterfaceDefinition node) {
    if (node.getParent() != null) {
      String parent = node.getParent().getCanonicalName();

      if (!typeNames.containsKey(parent)) {
        throw ParserException.undefined(node.getPosition(), parent);
      } else {
        Definition pd = typeNames.get(parent);

        if (!(pd instanceof InterfaceDefinition)) {
          throw SemanticException.illegalInterfaceParent(pd.getPosition());
        }

        InterfaceDefinition pid = (InterfaceDefinition) pd;
        pid.getChildren().add(node);
        node.setParentDefinition(pid);
      }
    }

    for (int i = 0; i < node.getFields().size(); i++) {
      node.getFields().get(i).accept(this);
    }
  }

  public void visit(Parameter node) {
    node.getType().accept(this);
  }

  public void visit(Function node) {
    node.getType().accept(this);
  }

  public void visit(ServiceDefinition node) {
    if (node.getParent() != null) {
      String parent = node.getParent().getCanonicalName();

      if (!typeNames.containsKey(parent)) {
        throw ParserException.undefined(node.getPosition(), parent);
      } else {
        Definition pd = typeNames.get(parent);

        if (!(pd instanceof ServiceDefinition)) {
          throw SemanticException.illegalServiceParent(pd.getPosition());
        }

        node.setParentDefinition((ServiceDefinition) pd);
      }
    }

    for (int i = 0; i < node.getFunctions().size(); i++) {
      node.getFunctions().get(i).accept(this);
    }
  }

}
