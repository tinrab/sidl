package com.moybl.sidl;

import com.moybl.sidl.ast.*;

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
			BaseType type = parseType();

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
		Position b = null;
		Token type = null;

		check(Token.KEYWORD_ENUM);
		Position a = current.getPosition();
		Identifier name = parseIdentifier();

		if (accept(Token.COLON)) {
			check(next.getToken());

			if (!current.getToken().isIntegerType()) {
				throw ParserException.expectedIntegerType(current.getPosition(), current.getToken());
			}

			type = current.getToken();
		}

		List<EnumValue> values = parseEnumList();

		if (values.size() != 0) {
			b = Position
					.expand(values.get(0).getPosition(), values.get(values.size() - 1)
							.getPosition());
		} else {
			b = current.getPosition();
		}

		return new EnumDefinition(Position.expand(a, b), name, type, values);
	}

	private List<EnumValue> parseEnumList() {
		check(Token.OPEN_BRACE);
		List<EnumValue> values = new ArrayList<EnumValue>();

		while (match(Token.IDENTIFIER)) {
			values.add(parseEnumValue());
			accept(Token.COMMA);
		}

		check(Token.CLOSE_BRACE);

		return values;
	}

	private EnumValue parseEnumValue() {
		Identifier name = parseIdentifier();
		Position a = name.getPosition();
		Position b = name.getPosition();
		String value = null;

		if (accept(Token.EQUALS)) {
			check(Token.LITERAL_INTEGER);
			b = current.getPosition();
			value = current.getLexeme();
		}

		return new EnumValue(Position.expand(a, b), name, value);
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
		if (accept(Token.OPEN_BRACKET)) {
			Position a = current.getPosition();

			if (accept(Token.LITERAL_INTEGER)) {
				int length = Integer.parseInt(current.getLexeme());
				check(Token.CLOSE_BRACKET);

				BaseType type = null;
				boolean isReference = accept(Token.ASTERISK);

				if(match(Token.IDENTIFIER)){
					type = new BaseType(parseIdentifier(), isReference);
				} else {
					check(next.getToken());
					type = new BaseType(current.getToken(), isReference);
				}

				return new ArrayType(Position.expand(a, current.getPosition()), length, type);
			} else {
				check(Token.CLOSE_BRACKET);
			}
		} else {

		}
	}

	private BaseType parsePrimaryType() {
		if (match(Token.IDENTIFIER)) {
			Identifier name = parseIdentifier();
			Position b = name.getPosition();

			return new BaseType(Position.expand(a, b), name);
		} else {
			if (!next.getToken().isType()) {
				throw ParserException.expectedType(current.getPosition(), next.getToken());
			}

			accept(next.getToken());
			Position b = current.getPosition();

			return new BaseType(Position.expand(a, b), current.getToken());
		}
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
