package com.moybl.sidl;

import com.moybl.sidl.ast.Definition;

import java.util.*;

public class Schema {

  private Map<String, List<Definition>> schema;
  private String topNamespace;

  public Schema() {
    schema = new HashMap<String, List<Definition>>();
    topNamespace = "";
  }

  public List<String> getNamespaces() {
    return new ArrayList<String>(schema.keySet());
  }

  public Map<String, List<Definition>> getRawSchema() {
    return schema;
  }

  public List<Definition> getDefinitions(String namespace) {
    return schema.get(namespace);
  }

  public void addDefinition(String namespace, Definition definition) {
    List<String> namePath = definition.getName().getPath();
    topNamespace = namePath.get(0);

    if (schema.containsKey(namespace)) {
      schema.get(namespace).add(definition);
    } else {
      List<Definition> definitions = new ArrayList<Definition>();
      definitions.add(definition);
      schema.put(namespace, definitions);
    }
  }

  public String getTopNamespace() {
    return topNamespace;
  }

}
