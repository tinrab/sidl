package com.moybl.sidl.ast;

import com.moybl.sidl.Position;

public class ArrayType extends Type {

	private int length;
	private PrimaryType type;

	public ArrayType(Position position, int length, PrimaryType type) {
		super(position);
		this.length = length;
		this.type = type;
	}

	public ArrayType(Position position) {
		super(position);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public int getLength() {
		return length;
	}

	public PrimaryType getType() {
		return type;
	}

}
