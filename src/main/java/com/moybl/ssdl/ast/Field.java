package com.moybl.ssdl.ast;

import com.moybl.ssdl.Position;

public class Field extends Node {

	private Identifier name;
	private Type type;

	public Field(Position position, Identifier name, Type type) {
		super(position);
		this.name = name;
		this.type = type;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public Identifier getName() {
		return name;
	}

	public Type getType() {
		return type;
	}

}
