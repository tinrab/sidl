package com.moybl.sidl.ast;

import com.moybl.sidl.Position;

public class NamespaceDefinition extends Definition {

	private Identifier name;

	public NamespaceDefinition(Position position, Identifier name) {
		super(position);
		this.name = name;
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

}
