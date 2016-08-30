package com.moybl.sidl.semantics;

import com.moybl.sidl.ParserException;
import com.moybl.sidl.Token;
import com.moybl.sidl.ast.*;

import java.util.HashMap;
import java.util.Map;

public class NameChecker implements Visitor {

	private Map<String, Definition> names;

	public NameChecker() {
		names = new HashMap<String, Definition>();
	}

	public void visit(Schema node) {
		for (int i = 0; i < node.getDefinitions().size(); i++) {
			Definition d = node.getDefinitions().get(i);
			names.put(d.getDefinedName(), d);
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
		if (node.getType().getName() != null) {
			if (!names.containsKey(node.getType().getName().getName())) {
				throw ParserException.undefined(node.getType().getName());
			}
		}
	}

	public void visit(ListType node) {
		if (node.getType().getName() != null) {
			if (!names.containsKey(node.getType().getName().getName())) {
				throw ParserException.undefined(node.getType().getName());
			}
		}
	}

	public void visit(Identifier node) {
		if (!names.containsKey(node.getName())) {
			throw ParserException.undefined(node);
		}
	}

	@Override
	public void visit(EnumValue node) {
	}

}
