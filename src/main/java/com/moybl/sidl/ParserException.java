package com.moybl.sidl;

public class ParserException extends RuntimeException {

  private Position position;

  public ParserException(String message) {
    super(message);
  }

  public ParserException(Position position, String message) {
    super(position.toString() + ": " + message);
    this.position = position;
  }

  public Position getPosition() {
    return position;
  }

  public static ParserException internal() {
    return new ParserException("Internal error");
  }

  public static ParserException unexpectedEof(Position position) {
    return new ParserException(position, "Unexpected EOF");
  }

  public static ParserException unexpected(Position position, Token expected, Token actual) {
    return new ParserException(position, String
        .format("Expected '%s', got '%s'", expected, actual));
  }

  public static ParserException expectedType(Position position, Token actual) {
    return new ParserException(position, String
        .format("Expected type, got '%s'", actual));
  }

  public static ParserException expectedLiteral(Position position, Token actual) {
    return new ParserException(position, String
        .format("Expected literal, got '%s'", actual));
  }

  public static ParserException expectedIntegerType(Position position, Token actual) {
    return new ParserException(position, String
        .format("Expected integer type, got '%s'", actual));
  }

  public static ParserException illegalStructType(Position position) {
    return new ParserException(position, "Illegal struct type");
  }

  public static ParserException illegalLeftHandSide(Position position) {
    return new ParserException(position, "Illegal left hand side value");
  }

  public static ParserException undefined(Position position, String name) {
    return new ParserException(position, String
        .format("'%s' not defined", name));
  }

  public static ParserException illegalMapKeyType(Position position, Token token) {
    return new ParserException(position, String
      .format("Illegal map key type '%s'", token.toString()));
  }

  public static ParserException enumInvalidOrder(Position position) {
    return new ParserException(position, "Enum values must be specified in ascending order");
  }

}
