package com.moybl.sidl.ast;

import com.moybl.sidl.Position;

public abstract class Definition extends Node {

	public Definition(Position position) {
		super(position);
	}

	public abstract Identifier getName();

	public abstract String getDefinedName();

}
