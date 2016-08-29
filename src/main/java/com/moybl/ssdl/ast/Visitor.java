package com.moybl.ssdl.ast;

public interface Visitor {

	void visit(Schema node);

	void visit(TypeDefinition node);

	void visit(EnumDefinition node);

	void visit(Field node);

	void visit(Type node);

	void visit(Identifier node);

}
