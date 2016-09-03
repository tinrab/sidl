package com.moybl.sidl.semantics;

import com.moybl.sidl.ParserException;
import com.moybl.sidl.ast.*;

import java.util.HashMap;
import java.util.Map;

public class NameChecker implements Visitor {

	private Map<String, Definition> typeNames;

	public NameChecker() {
		typeNames = new HashMap<String, Definition>();
	}

	public void visit(Document node) {
		for (int i = 0; i < node.getDefinitions().size(); i++) {
			Definition d = node.getDefinitions().get(i);

			if (d instanceof TypeDefinition || d instanceof EnumDefinition) {
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

}
