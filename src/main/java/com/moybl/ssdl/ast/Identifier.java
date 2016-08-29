package com.moybl.ssdl.ast;

import com.moybl.ssdl.Position;

public class Identifier extends Node {

	private String name;

	public Identifier(Position position, String name) {
		super(position);
		this.name = name;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public String getName() {
		return name;
	}

}
