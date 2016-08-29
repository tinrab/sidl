package com.moybl.ssdl;

import com.moybl.ssdl.ast.Identifier;

public class ParserException extends RuntimeException {

	private Position position;

	public ParserException(String message) {
		super(message);
	}

	public ParserException(Position position, String message) {
		super(message);

		this.position = position;
	}

	public Position getPosition() {
		return position;
	}

	public static ParserException internal() {
		return new ParserException("Internal error");
	}

	public static ParserException unexpected(Position position, Token expected, Token actual) {
		return new ParserException(position, String
				.format("Expected '%s', got '%s'", expected, actual));
	}

	public static ParserException expectedType(Position position, Token actual) {
		return new ParserException(position, String
				.format("Expected type, got '%s'", actual));
	}

	public static ParserException illegalLeftHandSide(Position position) {
		return new ParserException(position, "Illegal left hand side value");
	}

	public static ParserException undefined(Identifier name) {
		return new ParserException(name.getPosition(), String
				.format("'%s' not defined", name.getName()));
	}

}
