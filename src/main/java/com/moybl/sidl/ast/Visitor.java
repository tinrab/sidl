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

	void visit(PrimaryType node);

	void visit(Namespace node);

	void visit(NamespaceDefinition node);

	void visit(Use node);

}
