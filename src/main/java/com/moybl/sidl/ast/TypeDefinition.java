package com.moybl.sidl.ast;

import com.moybl.sidl.Position;

import java.util.List;

public class TypeDefinition extends Definition {

	private Identifier name;
	private Identifier parent;
	private Definition parentDefinition;
	private Identifier oldName;
	private Type type;
	private List<Field> fields;

	public TypeDefinition(Position position, Identifier name, Identifier oldName) {
		super(position);
		this.name = name;
		this.oldName = oldName;
	}

	public TypeDefinition(Position position, Identifier name, Type type) {
		super(position);
		this.name = name;
		this.type = type;
	}

	public TypeDefinition(Position position, Identifier name, Identifier parent, List<Field> fields) {
		super(position);
		this.name = name;
		this.parent = parent;
		this.fields = fields;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public Identifier getName() {
		return name;
	}

	public Identifier getParent() {
		return parent;
	}

	public Definition getParentDefinition() {
		return parentDefinition;
	}

	public void setParentDefinition(Definition parentDefinition) {
		this.parentDefinition = parentDefinition;
	}

	public String getDefinedName() {
		return name.getCanonicalName();
	}

	public Identifier getOldName() {
		return oldName;
	}

	public Type getType() {
		return type;
	}

	public List<Field> getFields() {
		return fields;
	}

}
