package com.moybl.sidl;

import com.moybl.sidl.ast.*;

import java.util.*;

public class Parser {

  private Lexer lexer;
  private Symbol current;
  private Symbol next;

  private List<String> currentPath;

  public Parser(Lexer lexer) {
    this.lexer = lexer;
  }

  public Document parse() {
    next = lexer.next();

    Position a = next.getPosition();
    List<Definition> definitions = parseDefinitions();

    if (definitions.size() == 0) {
      return new Document(new Position(0, 0), definitions);
    }

    return new Document(Position.expand(a, current.getPosition()), definitions);
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
    if (next.getToken() == Token.KEYWORD_NAMESPACE) {
      return parseNamespaceDefinition();
    }

    Definition def = null;
    Map<String, Attribute> attributes = parseAttributeList();

    if (next.getToken() == Token.KEYWORD_CLASS) {
      def = parseClassDefinition();
    } else if (next.getToken() == Token.KEYWORD_STRUCT) {
      def = parseStructDefinition();
    } else if (next.getToken() == Token.KEYWORD_INTERFACE) {
      def = parseInterfaceDefinition();
    } else if (next.getToken() == Token.KEYWORD_ENUM) {
      def = parseEnumDefinition();
    } else if (next.getToken() == Token.KEYWORD_SERVICE) {
      def = parseServiceDefinition();
    } else {
      throw ParserException.internal();
    }

    def.setAttributes(attributes);

    return def;
  }

  private NamespaceDefinition parseNamespaceDefinition() {
    check(Token.KEYWORD_NAMESPACE);
    Position a = current.getPosition();
    Identifier name = parseIdentifier();

    currentPath = name.getPath();

    return new NamespaceDefinition(Position.expand(a, current.getPosition()), name);
  }

  private InterfaceDefinition parseInterfaceDefinition() {
    check(Token.KEYWORD_INTERFACE);
    Position a = current.getPosition();
    Identifier name = parseIdentifier();
    Identifier parent = null;

    if (accept(Token.COLON)) {
      parent = parseIdentifier();
    }

    check(Token.OPEN_BRACE);
    List<Field> fields = parseClassFieldList();
    check(Token.CLOSE_BRACE);

    return new InterfaceDefinition(Position
      .expand(a, current.getPosition()), name, parent, fields);
  }

  private ClassDefinition parseClassDefinition() {
    check(Token.KEYWORD_CLASS);
    Position a = current.getPosition();
    Identifier name = parseIdentifier();

    Identifier parent = null;

    if (accept(Token.COLON)) {
      parent = parseIdentifier();
    }

    check(Token.OPEN_BRACE);
    List<Field> fields = parseClassFieldList();
    check(Token.CLOSE_BRACE);

    return new ClassDefinition(Position.expand(a, current.getPosition()), name, parent, fields);
  }

  private StructDefinition parseStructDefinition() {
    check(Token.KEYWORD_STRUCT);
    Position a = current.getPosition();
    Identifier name = parseIdentifier();

    check(Token.OPEN_BRACE);
    List<Field> fields = parseStructFieldList();
    check(Token.CLOSE_BRACE);

    return new StructDefinition(Position.expand(a, current.getPosition()), name, fields);
  }

  private ServiceDefinition parseServiceDefinition() {
    check(Token.KEYWORD_SERVICE);
    Position a = current.getPosition();
    Identifier name = parseIdentifier();

    Identifier parent = null;

    if (accept(Token.COLON)) {
      parent = parseIdentifier();
    }

    check(Token.OPEN_BRACE);
    List<Function> functions = parseFunctionList();
    check(Token.CLOSE_BRACE);

    return new ServiceDefinition(Position
      .expand(a, current.getPosition()), name, parent, functions);
  }

  private EnumDefinition parseEnumDefinition() {
    Position b = null;
    Token type = Token.TYPE_UINT8;

    check(Token.KEYWORD_ENUM);
    Position a = current.getPosition();
    Identifier name = parseIdentifier();

    if (next.getToken().isIntegerType()) {
      check(next.getToken());
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

    long next = 0;

    while (match(Token.IDENTIFIER)) {
      EnumValue ev = parseEnumValue();

      if (ev.getValue() != null && ev.getValue().getKind() != Literal.Kind.INTEGER) {
        throw ParserException.expectedIntegerType(current.getPosition(), current.getToken());
      }

      if (ev.getValue() == null) {
        ev.setValue(new Literal(ev.getPosition(), Literal.Kind.INTEGER, next));
        next++;
      } else {
        long val = ev.getValue().getLongValue();

        if (next != 0 && val <= next) {
          throw ParserException.enumInvalidOrder(ev.getPosition());
        }

        next = val + 1;
      }

      values.add(ev);
      accept(Token.COMMA);
    }

    check(Token.CLOSE_BRACE);

    return values;
  }

  private EnumValue parseEnumValue() {
    check(Token.IDENTIFIER);
    String name = current.getLexeme();
    Position a = current.getPosition();
    Literal value = null;

    if (accept(Token.EQUALS)) {
      value = parseLiteral();
    }

    return new EnumValue(Position.expand(a, current.getPosition()), name, value);
  }

  private Field parseStructField() {
    Map<String, Attribute> attributes = parseAttributeList();
    check(Token.IDENTIFIER);
    String name = current.getLexeme();
    Position a = current.getPosition();

    PrimaryType type = parsePrimaryType();

    return new Field(Position.expand(a, type.getPosition()), attributes, name, type);
  }

  private List<Field> parseStructFieldList() {
    List<Field> fields = new ArrayList<Field>();

    while (match(Token.IDENTIFIER) || match(Token.AT)) {
      fields.add(parseStructField());
      accept(Token.COMMA);
    }

    return fields;
  }

  private Field parseClassField() {
    Map<String, Attribute> attributes = parseAttributeList();
    check(Token.IDENTIFIER);
    String name = current.getLexeme();
    Position a = current.getPosition();

    Type type = parseType();

    return new Field(Position.expand(a, type.getPosition()), attributes, name, type);
  }

  private List<Field> parseClassFieldList() {
    List<Field> fields = new ArrayList<Field>();

    while (match(Token.IDENTIFIER) || match(Token.AT)) {
      fields.add(parseClassField());
      accept(Token.COMMA);
    }

    return fields;
  }

  private Type parseType() {
    if (accept(Token.OPEN_BRACKET)) {
      Position a = current.getPosition();

      if (accept(Token.LITERAL_INTEGER)) {
        int length = Integer.parseInt(current.getLexeme());
        check(Token.CLOSE_BRACKET);

        return new ArrayType(Position
          .expand(a, current.getPosition()), length, parsePrimaryType());
      } else {
        check(Token.CLOSE_BRACKET);

        return new VectorType(Position.expand(a, current.getPosition()), parsePrimaryType());
      }
    } else if (accept(Token.LESS)) {
      Position a = current.getPosition();
      PrimaryType keyType = parsePrimaryType();
      check(Token.COMMA);
      PrimaryType valueType = parsePrimaryType();
      check(Token.GREATER);

      return new MapType(Position.expand(a, current.getPosition()), keyType, valueType);
    }

    return parsePrimaryType();
  }

  private PrimaryType parsePrimaryType() {
    if (match(Token.IDENTIFIER) | match(Token.DOT)) {
      Identifier name = parseAccessIdentifier();

      return new PrimaryType(name.getPosition(), name);
    }

    if (!next.getToken().isType()) {
      throw ParserException.expectedType(next.getPosition(), next.getToken());
    }

    check(next.getToken());

    return new PrimaryType(current.getPosition(), current.getToken());
  }

  private Map<String, Attribute> parseAttributeList() {
    Map<String, Attribute> attributes = new HashMap<String, Attribute>();

    while (match(Token.AT)) {
      Attribute attribute = parseAttribute();
      attributes.put(attribute.getName(), attribute);
    }

    return attributes;
  }

  private Attribute parseAttribute() {
    check(Token.AT);
    Position a = current.getPosition();
    check(Token.IDENTIFIER);
    String name = current.getLexeme();

    Map<String, AttributeEntry> entries = new HashMap<String, AttributeEntry>();

    if (accept(Token.OPEN_PARENTHESIS)) {
      while (match(Token.IDENTIFIER) || next.getToken().isLiteral()) {
        AttributeEntry entry = parseAttributeEntry();
        entries.put(entry.getName(), entry);
        accept(Token.COMMA);
      }

      check(Token.CLOSE_PARENTHESIS);
    }

    return new Attribute(Position.expand(a, current.getPosition()), name, entries);
  }

  private AttributeEntry parseAttributeEntry() {
    String name = null;
    Position a = null;

    if (accept(Token.IDENTIFIER)) {
      a = current.getPosition();
      name = current.getLexeme();
      check(Token.EQUALS);
    }

    Literal value = parseLiteral();

    if (a == null) {
      a = current.getPosition();
    }

    return new AttributeEntry(Position.expand(a, current.getPosition()), name, value);
  }

  private Function parseFunction() {
    check(Token.IDENTIFIER);
    Position a = current.getPosition();
    String name = current.getLexeme();
    check(Token.OPEN_PARENTHESIS);
    List<Parameter> parameters = parseParameterList();
    check(Token.CLOSE_PARENTHESIS);
    Type type = parseType();

    return new Function(Position.expand(a, current.getPosition()), name, parameters, type);
  }

  private List<Function> parseFunctionList() {
    List<Function> functions = new ArrayList<Function>();

    while (match(Token.IDENTIFIER)) {
      functions.add(parseFunction());
      accept(Token.COMMA);
    }

    return functions;
  }

  private List<Parameter> parseParameterList() {
    List<Parameter> parameters = new ArrayList<Parameter>();

    do {
      check(Token.IDENTIFIER);
      Position a = current.getPosition();
      String name = current.getLexeme();
      Type type = parseType();
      parameters.add(new Parameter(Position.expand(a, current.getPosition()), name, type));
    } while (accept(Token.COMMA));

    return parameters;
  }

  private Literal parseLiteral() {
    Literal.Kind kind = Literal.Kind.INTEGER;
    Object value = null;

    if (accept(Token.LITERAL_INTEGER)) {
      kind = Literal.Kind.INTEGER;
      value = Long.parseLong(current.getLexeme());
    } else if (accept(Token.LITERAL_STRING)) {
      kind = Literal.Kind.STRING;
      value = current.getLexeme().substring(1, current.getLexeme().length() - 1);
    } else if (accept(Token.LITERAL_FLOAT)) {
      kind = Literal.Kind.FLOAT;
      value = Double.parseDouble(current.getLexeme());
    } else {
      throw ParserException.expectedLiteral(current.getPosition(), current.getToken());
    }

    return new Literal(current.getPosition(), kind, value);
  }

  private Identifier parseIdentifier() {
    Position a = next.getPosition();
    List<String> path = new ArrayList<String>();

    if (currentPath != null) {
      path.addAll(currentPath);
    }

    while (accept(Token.IDENTIFIER)) {
      path.add(current.getLexeme());

      if (!accept(Token.DOT)) {
        break;
      }
    }

    return new Identifier(Position.expand(a, current.getPosition()), path);
  }

  private Identifier parseAccessIdentifier() {
    Position a = next.getPosition();
    List<String> path = new ArrayList<String>();

    if (accept(Token.DOT)) {
      if (currentPath != null) {
        path.addAll(currentPath);
      }
    }

    while (accept(Token.IDENTIFIER)) {
      path.add(current.getLexeme());

      if (!accept(Token.DOT)) {
        break;
      }
    }

    if (path.size() == 1 && currentPath != null) {
      path.addAll(0, currentPath);
    }

    return new Identifier(Position.expand(a, current.getPosition()), path);
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
