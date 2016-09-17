package com.moybl.sidl.ast;

public interface Visitor {

  void visit(Document node);

  void visit(TypeDefinition node);

  void visit(EnumDefinition node);

  void visit(Field node);

  void visit(ArrayType node);

  void visit(ListType node);

  void visit(Identifier node);

  void visit(EnumValue node);

  void visit(PrimaryType node);

  void visit(NamespaceDefinition node);

  void visit(Attribute node);

  void visit(AttributeEntry node);

  void visit(Literal node);

  void visit(InterfaceDefinition node);

  void visit(Parameter node);

  void visit(Function node);

  void visit(ServiceDefinition node);

}
