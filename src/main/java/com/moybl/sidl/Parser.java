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

		List<Node> nodes = new ArrayList<Node>();
		Position a = next.getPosition();

		while (!match(Token.EOF)) {
			if (match(Token.KEYWORD_USE)) {
				nodes.add(parseUse());
			} else if (match(Token.KEYWORD_NAMESPACE)) {
				nodes.add(parseNamespaceDefinition());
			} else {
				Definition definition = parseDefinition();

				if (definition != null) {
					nodes.add(definition);
				}
			}
		}

		return new Schema(Position.expand(a, current.getPosition()), nodes);
	}

	private Use parseUse() {
		check(Token.KEYWORD_USE);
		Position a = current.getPosition();
		Namespace namespace = parseNamespace();

		return new Use(Position.expand(a, current.getPosition()), namespace);
	}

	private NamespaceDefinition parseNamespaceDefinition() {
		check(Token.KEYWORD_NAMESPACE);
		Position a = current.getPosition();
		Namespace namespace = parseNamespace();

		return new NamespaceDefinition(Position.expand(a, current.getPosition()), namespace);
	}

	private Namespace parseNamespace() {
		List<Identifier> path = new ArrayList<Identifier>();

		do {
			path.add(parseIdentifier());
		} while (accept(Token.COLON));

		return new Namespace(Position.expand(path.get(0).getPosition(), current.getPosition()), path);
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

				return new ArrayType(Position.expand(a, current.getPosition()), length, parseType());
			} else {
				check(Token.CLOSE_BRACKET);

				return new ListType(Position.expand(a, current.getPosition()), parseType());
			}
		}

		return parsePrimaryType();
	}

	private PrimaryType parsePrimaryType() {
		if (accept(Token.ASTERISK)) {
			Position a = current.getPosition();
			Identifier name = parseIdentifier();

			return new PrimaryType(Position.expand(a, name.getPosition()), name, true);
		}

		if (match(Token.IDENTIFIER)) {
			Identifier name = parseIdentifier();

			return new PrimaryType(name.getPosition(), name, false);
		}

		if (!next.getToken().isType()) {
			throw ParserException.expectedType(next.getPosition(), next.getToken());
		}

		check(next.getToken());

		return new PrimaryType(current.getPosition(), current.getToken(), false);
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
