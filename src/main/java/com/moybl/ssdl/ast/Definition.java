package com.moybl.ssdl.ast;

import com.moybl.ssdl.Position;

public abstract class Definition extends Node {

	public Definition(Position position) {
		super(position);
	}

	public abstract String getDefinedName();

}
