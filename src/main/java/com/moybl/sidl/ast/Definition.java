package com.moybl.sidl.ast;

import com.moybl.sidl.Position;

public abstract class Definition extends Node {

	public Definition(Position position) {
		super(position);
	}

	public abstract String getDefinedName();

}
