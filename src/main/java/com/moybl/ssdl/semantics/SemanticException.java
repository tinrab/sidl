package com.moybl.ssdl.semantics;

import com.moybl.ssdl.Position;

public class SemanticException extends RuntimeException {

	private Position position;

	public SemanticException(String message) {
		super(message);
	}

	public SemanticException(Position position, String message) {
		super(message);

		this.position = position;
	}

	public Position getPosition() {
		return position;
	}

	public static SemanticException internal() {
		return new SemanticException("Internal error");
	}

	public static SemanticException illegalInsert() {
		return new SemanticException("Illegal insert");
	}

	public static SemanticException illegalRemove() {
		return new SemanticException("Illegal remove");
	}

	public static SemanticException undefined(String name) {
		return new SemanticException("Undefined '" + name + "'");
	}

}
