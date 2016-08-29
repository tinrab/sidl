package com.moybl.ssdl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Lexer {

	private InputStream stream;
	private int line;
	private int column;

	private static final Map<String, Token> RESERVED_WORDS = new HashMap<String, Token>() {{
		put("type", Token.KEYWORD_TYPE);
		put("enum", Token.KEYWORD_ENUM);
		put("s", Token.TYPE_STRING);
		put("i", Token.TYPE_INT);
		put("i8", Token.TYPE_INT8);
		put("i16", Token.TYPE_INT16);
		put("i32", Token.TYPE_INT32);
		put("i64", Token.TYPE_INT64);
		put("u", Token.TYPE_UINT);
		put("u8", Token.TYPE_UINT8);
		put("u16", Token.TYPE_UINT16);
		put("u32", Token.TYPE_UINT32);
		put("u64", Token.TYPE_UINT64);
		put("f32", Token.TYPE_FLOAT32);
		put("f64", Token.TYPE_FLOAT64);
	}};
	private static final Map<String, Token> PUNCTUATORS = new HashMap<String, Token>() {{
		put("*", Token.ASTERISK);
		put(",", Token.COMMA);
		put("{", Token.OPEN_BRACE);
		put("}", Token.CLOSE_BRACE);
		put("[]", Token.BRACKETS);
	}};

	public Lexer(InputStream stream) {
		this.stream = stream;
		line = 1;
	}

	public Symbol next() {
		Symbol symbol = null;

		try {
			int ch;

			while ((ch = stream.read()) != -1) {
				column++;

				if (ch == '/') {
					stream.mark(1);
					int next = stream.read();

					if (next == '/') {
						do {
							ch = stream.read();
						} while (ch != '\n' && ch != '\r' && ch != -1);

						line++;
						column = 0;

						continue;
					} else if (next == '*') {
						int p;

						do {
							p = ch;
							ch = stream.read();

							if (ch == -1) {
								throw LexerException.unclosedComment(line, column);
							}

							column++;

							if (ch == '\n' || ch == '\r') {
								line++;
								column = 0;
							}
						} while (p != '*' || ch != '/');

						continue;
					}

					stream.reset();
				}

				if (Character.isLetter(ch) || ch == '_') {
					StringBuilder sb = new StringBuilder();

					do {
						sb.append((char) ch);
						stream.mark(1);
						ch = stream.read();
					} while (Character.isLetterOrDigit(ch) || ch == '_');

					stream.reset();

					String lexeme = sb.toString();
					Token token = null;

					if (RESERVED_WORDS.containsKey(lexeme)) {
						token = RESERVED_WORDS.get(lexeme);
					} else {
						token = Token.IDENTIFIER;
					}

					int endColumn = column + lexeme.length();
					symbol = new Symbol(token, lexeme, line, column, endColumn);
					column = endColumn - 1;

					break;
				} else if (!Character.isWhitespace(ch)) {
					String lexeme = Character.toString((char) ch);
					Token token = PUNCTUATORS.get(lexeme);

					if (token == null) {
						stream.mark(1);
						ch = stream.read();
						String newLexeme = lexeme + Character.toString((char) ch);

						if (PUNCTUATORS.containsKey(newLexeme)) {
							token = PUNCTUATORS.get(newLexeme);
							lexeme = newLexeme;
						} else {
							stream.reset();
						}
					}

					if (token == null) {
						throw LexerException.illegalCharacter(line, column, ch);
					}

					int endColumn = column + lexeme.length();
					symbol = new Symbol(token, lexeme, line, column, endColumn);
					column = endColumn;

					break;
				} else if (ch == '\n' || ch == '\r') {
					line++;
					column = 0;

					stream.mark(1);
					int next = stream.read();

					if (next != '\r' && next != '\n') {
						stream.reset();
					}
				}
			}
		} catch (IOException e) {
			throw LexerException.message(e.getMessage());
		}

		if (symbol == null) {
			symbol = new Symbol(Token.EOF, null, getCurrentPosition());
		}

		return symbol;
	}

	private Position getCurrentPosition() {
		return new Position(line, column);
	}

}
