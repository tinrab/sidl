package com.moybl.sidl.ast;

import com.moybl.sidl.Position;

import java.util.List;

public class Attribute extends Node {

	private String name;
	private List<AttributeEntry> entries;

	public Attribute(Position position, String name, List<AttributeEntry> entries) {
		super(position);
		this.name = name;
		this.entries = entries;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public String getName() {
		return name;
	}

	public List<AttributeEntry> getEntries() {
		return entries;
	}

}
