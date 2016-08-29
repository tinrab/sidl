package com.moybl.ssdl.semantics;

import com.moybl.ssdl.ParserException;
import com.moybl.ssdl.Token;
import com.moybl.ssdl.ast.*;

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

	public void visit(Type node) {
		if (node.getToken() == Token.IDENTIFIER) {
			node.getName().accept(this);
		}
	}

	public void visit(Identifier node) {
		if (!names.containsKey(node.getName())) {
			throw ParserException.undefined(node);
		}
	}

}
