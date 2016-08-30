package com.moybl.sidl;

import com.moybl.sidl.ast.Definition;

import java.util.*;

public class Schema {

	private Map<String, List<Definition>> schema;

	public Schema() {
		schema = new HashMap<String, List<Definition>>();
	}

	public List<String> getNamespaces() {
		return new ArrayList<String>(schema.keySet());
	}

	public List<Definition> getDefinitions(String namespace) {
		return schema.get(namespace);
	}

	public void addDefinition(String namespace, Definition definition) {
		if (schema.containsKey(namespace)) {
			schema.get(namespace).add(definition);
		} else {
			List<Definition> definitions = new ArrayList<Definition>();
			definitions.add(definition);
			schema.put(namespace, definitions);
		}
	}

}
