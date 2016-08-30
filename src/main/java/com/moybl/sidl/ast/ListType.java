package com.moybl.sidl.ast;

import com.moybl.sidl.Position;

public class ListType extends Type {

	private Type type;

	public ListType(Position position, Type type) {
		super(position);
		this.type = type;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public Type getType() {
		return type;
	}

}
