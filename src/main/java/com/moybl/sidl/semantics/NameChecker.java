package com.moybl.sidl.semantics;

import com.moybl.sidl.ParserException;
import com.moybl.sidl.ast.*;

import java.util.HashMap;
import java.util.Map;

public class NameChecker implements Visitor {

	private Map<String, Definition> names;

	public NameChecker() {
		names = new HashMap<String, Definition>();
	}

	public void visit(Schema node) {
		for (int i = 0; i < node.getNodes().size(); i++) {
			Node n = node.getNodes().get(i);

			if (n instanceof Definition) {
				Definition d = (Definition) n;
				names.put(d.getDefinedName(), d);
			}
		}

		for (int i = 0; i < node.getNodes().size(); i++) {
			node.getNodes().get(i).accept(this);
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
		if (!names.containsKey(node.getName())) {
			throw ParserException.undefined(node.getPosition(), node.getName());
		}
	}

	public void visit(EnumValue node) {
	}

	public void visit(PrimaryType node) {
		if (node.getName() != null) {
			node.getName().accept(this);
		}
	}

	public void visit(Namespace node) {
	}

	public void visit(NamespaceDefinition node) {
	}

	public void visit(Use node) {
		String name = node.getNamespace().toString();

		if (!names.containsKey(name)) {
			throw ParserException.undefined(node.getPosition(), name);
		}
	}

}
