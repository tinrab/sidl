package com.moybl.sidl.ast;

import com.moybl.sidl.Position;

public class NamespaceDefinition extends Definition {

	private Namespace namespace;

	public NamespaceDefinition(Position position, Namespace namespace) {
		super(position);
		this.namespace = namespace;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public Namespace getNamespace() {
		return namespace;
	}

	public String getDefinedName() {
		return namespace.toString();
	}

}
