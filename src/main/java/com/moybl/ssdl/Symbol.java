package com.moybl.ssdl;

public class Symbol {

	private Token token;
	private String lexeme;
	private Position position;

	public Symbol(Token token, String lexeme, Position position) {
		this.token = token;
		this.lexeme = lexeme;
		this.position = position;
	}

	public Symbol(Token token, String lexeme, int startLine, int endLine, int startColumn, int endColumn) {
		this.token = token;
		this.lexeme = lexeme;
		this.position = new Position(startLine, endLine, startColumn, endColumn);
	}

	public Symbol(Token token, String lexeme, int line, int startColumn, int endColumn) {
		this.token = token;
		this.lexeme = lexeme;
		this.position = new Position(line, line, startColumn, endColumn);
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public String getLexeme() {
		return lexeme;
	}

	public void setLexeme(String lexeme) {
		this.lexeme = lexeme;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

}
