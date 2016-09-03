package com.moybl.sidl.ast;

import com.moybl.sidl.Position;

import java.util.List;

public abstract class Definition extends Node {

	private List<Attribute> attributes;

	public Definition(Position position) {
		super(position);
	}

	public abstract Identifier getName();

	public abstract String getDefinedName();

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

}
