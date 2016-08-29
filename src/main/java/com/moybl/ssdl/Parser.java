package com.moybl.ssdl;

import com.moybl.ssdl.ast.*;

import java.util.ArrayList;
import java.util.List;

public class Parser {

	private Lexer lexer;
	private Symbol current;
	private Symbol next;

	public Parser(Lexer lexer) {
		this.lexer = lexer;
	}

	public Schema parse() {
		next = lexer.next();

		List<Definition> definitions = parseDefinitions();
		Position p = Position
				.expand(definitions.get(0).getPosition(), definitions.get(definitions.size() - 1)
						.getPosition());

		return new Schema(p, definitions);
	}

	private List<Definition> parseDefinitions() {
		List<Definition> definitions = new ArrayList<Definition>();

		while (!match(Token.EOF)) {
			Definition definition = parseDefinition();

			if (definition != null) {
				definitions.add(definition);
			}
		}

		return definitions;
	}

	private Definition parseDefinition() {
		switch (next.getToken()) {
			case KEYWORD_TYPE:
				return parseTypeDefinition();
			case KEYWORD_ENUM:
				return parseEnumDefinition();
		}

		throw ParserException.internal();
	}

	private TypeDefinition parseTypeDefinition() {
		check(Token.KEYWORD_TYPE);
		Position a = current.getPosition();
		Identifier name = parseIdentifier();

		if (match(Token.IDENTIFIER)) {
			Identifier oldName = parseIdentifier();

			return new TypeDefinition(Position.expand(a, oldName.getPosition()), name, oldName);
		} else if (next.getToken().isType()) {
			Type type = parseType();

			return new TypeDefinition(Position.expand(a, type.getPosition()), name, type);
		}

		List<Field> fields = parseFieldList();
		Position b = null;
		if (fields.size() != 0) {
			b = Position
					.expand(fields.get(0).getPosition(), fields.get(fields.size() - 1)
							.getPosition());
		} else {
			b = current.getPosition();
		}

		return new TypeDefinition(Position.expand(a, b), name, fields);
	}

	private EnumDefinition parseEnumDefinition() {
		check(Token.KEYWORD_ENUM);
		Position a = current.getPosition();
		Identifier name = parseIdentifier();
		List<Identifier> values = parseIdentifierList();

		Position b = null;

		if (values.size() != 0) {
			b = Position
					.expand(values.get(0).getPosition(), values.get(values.size() - 1)
							.getPosition());
		} else {
			b = current.getPosition();
		}

		return new EnumDefinition(Position.expand(a, b), name, values);
	}

	private Field parseField() {
		Identifier name = parseIdentifier();
		Type type = parseType();

		return new Field(Position.expand(name.getPosition(), type.getPosition()), name, type);
	}

	private List<Field> parseFieldList() {
		check(Token.OPEN_BRACE);
		List<Field> fields = new ArrayList<Field>();

		while (match(Token.IDENTIFIER)) {
			fields.add(parseField());
			accept(Token.COMMA);
		}

		check(Token.CLOSE_BRACE);

		return fields;
	}

	private Type parseType() {
		Position a = current.getPosition();
		boolean isList = accept(Token.BRACKETS);

		if (match(Token.OPEN_BRACE)) {
			List<Field> fields = parseFieldList();
			Position b = null;

			if (fields.size() != 0) {
				b = Position
						.expand(fields.get(0).getPosition(), fields.get(fields.size() - 1)
								.getPosition());
			} else {
				b = current.getPosition();
			}

			return new Type(Position.expand(a, b), fields);
		}

		boolean isReference = accept(Token.ASTERISK);

		if (match(Token.IDENTIFIER)) {
			Identifier name = parseIdentifier();
			Position b = name.getPosition();

			return new Type(Position.expand(a, b), name, isList, isReference);
		} else {
			if (!next.getToken().isType()) {
				throw ParserException.expectedType(current.getPosition(), next.getToken());
			}

			accept(next.getToken());
			Position b = current.getPosition();

			return new Type(Position.expand(a, b), isList, isReference, current.getToken());
		}
	}

	private List<Identifier> parseIdentifierList() {
		check(Token.OPEN_BRACE);
		List<Identifier> identifiers = new ArrayList<Identifier>();

		while (match(Token.IDENTIFIER)) {
			identifiers.add(parseIdentifier());
			accept(Token.COMMA);
		}

		check(Token.CLOSE_BRACE);

		return identifiers;
	}

	private Identifier parseIdentifier() {
		check(Token.IDENTIFIER);

		return new Identifier(current.getPosition(), current.getLexeme());
	}

	private boolean match(Token token) {
		return next.getToken() == token;
	}

	private boolean match(Token... tokens) {
		for (int i = 0; i < tokens.length; i++) {
			if (match(tokens[i])) {
				return true;
			}
		}

		return false;
	}

	private void check(Token token) {
		if (match(token)) {
			current = next;
			next = lexer.next();
		} else {
			throw ParserException.unexpected(current.getPosition(), token, next.getToken());
		}
	}

	private void check(Token... tokens) {
		for (int i = 0; i < tokens.length; i++) {
			check(tokens[i]);
		}
	}

	private boolean accept(Token token) {
		if (match(token)) {
			current = next;
			next = lexer.next();

			return true;
		}

		return false;
	}

	private boolean accept(Token... tokens) {
		for (int i = 0; i < tokens.length; i++) {
			if (accept(tokens[i])) {
				return true;
			}
		}

		return false;
	}

}
