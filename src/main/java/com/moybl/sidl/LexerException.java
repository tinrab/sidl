package com.moybl.sidl;

public class LexerException extends RuntimeException {

  private Position position;

  public LexerException(String message) {
    super(message);
  }

  public LexerException(int line, int column, String message) {
    super(new Position(line, column).toString() + ": " + message);
    position = new Position(line, column);
  }

  public Position getPosition() {
    return position;
  }

  public static LexerException message(String message) {
    return new LexerException(message);
  }

  public static LexerException unclosedString(int line, int column) {
    return new LexerException(line, column, "Unclosed string literal");
  }

  public static LexerException illegalCharacter(int line, int column, int ch) {
    return new LexerException(line, column, "Illegal character '" + ((char) ch) + "'");
  }

  public static LexerException unclosedComment(int line, int column) {
    return new LexerException(line, column, "Unclosed comment");
  }

}
