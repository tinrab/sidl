package com.moybl.sidl.ast;

public interface Visitor {

	void visit(Schema node);

	void visit(TypeDefinition node);

	void visit(EnumDefinition node);

	void visit(Field node);

	void visit(ArrayType node);

	void visit(ListType node);

	void visit(Identifier node);

	void visit(EnumValue node);

}
