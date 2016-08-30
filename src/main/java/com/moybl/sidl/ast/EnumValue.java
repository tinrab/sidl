package com.moybl.sidl.ast;

import com.moybl.sidl.Position;

public class EnumValue extends Node {

	private String name;
	private String value;

	public EnumValue(Position position, String name, String value) {
		super(position);
		this.name = name;
		this.value = value;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

}
