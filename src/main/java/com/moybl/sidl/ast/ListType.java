package com.moybl.sidl.ast;

import com.moybl.sidl.Position;

public class ListType extends Type {

	private BaseType type;

	public ListType(Position position, BaseType type) {
		super(position);
		this.type = type;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public BaseType getType() {
		return type;
	}

}
