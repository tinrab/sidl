package com.moybl.sidl.ast;

import com.moybl.sidl.Position;

public class EnumValue extends Node {

	private Identifier name;
	private String value;

	public EnumValue(Position position, Identifier name, String value) {
		super(position);
		this.name = name;
		this.value = value;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public Identifier getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

}
