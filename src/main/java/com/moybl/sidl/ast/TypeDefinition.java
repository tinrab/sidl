package com.moybl.sidl.ast;

import com.moybl.sidl.Position;

import java.util.List;

public class TypeDefinition extends Definition {

	private Identifier name;
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

	public TypeDefinition(Position position, Identifier name, List<Field> fields) {
		super(position);
		this.name = name;
		this.fields = fields;
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
