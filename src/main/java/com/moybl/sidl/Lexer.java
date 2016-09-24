package com.moybl.sidl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Lexer {

  private InputStream stream;
  private int line;
  private int column;

  private static final Map<String, Token> RESERVED_WORDS = new HashMap<String, Token>() {{
    put("class", Token.KEYWORD_CLASS);
    put("struct", Token.KEYWORD_STRUCT);
    put("enum", Token.KEYWORD_ENUM);
    put("s", Token.TYPE_STRING);
    put("i", Token.TYPE_INT32);
    put("i8", Token.TYPE_INT8);
    put("i16", Token.TYPE_INT16);
    put("i32", Token.TYPE_INT32);
    put("i64", Token.TYPE_INT64);
    put("u", Token.TYPE_UINT32);
    put("u8", Token.TYPE_UINT8);
    put("u16", Token.TYPE_UINT16);
    put("u32", Token.TYPE_UINT32);
    put("u64", Token.TYPE_UINT64);
    put("f32", Token.TYPE_FLOAT32);
    put("f64", Token.TYPE_FLOAT64);
    put("b", Token.TYPE_BOOL);
    put("namespace", Token.KEYWORD_NAMESPACE);
    put("interface", Token.KEYWORD_INTERFACE);
    put("service", Token.KEYWORD_SERVICE);
  }};
  private static final Map<String, Token> PUNCTUATORS = new HashMap<String, Token>() {{
    put("*", Token.ASTERISK);
    put(",", Token.COMMA);
    put(":", Token.COLON);
    put(".", Token.DOT);
    put("=", Token.EQUALS);
    put("{", Token.OPEN_BRACE);
    put("}", Token.CLOSE_BRACE);
    put("[", Token.OPEN_BRACKET);
    put("]", Token.CLOSE_BRACKET);
    put("(", Token.OPEN_PARENTHESIS);
    put(")", Token.CLOSE_PARENTHESIS);
    put("@", Token.AT);
    put("<", Token.LESS);
    put(">", Token.GREATER);
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

        if (Character.isDigit(ch)) {
          StringBuilder sb = new StringBuilder();
          Token token = Token.LITERAL_INTEGER;
          int endColumn = column;

          sb.append((char) ch);
          boolean isScientific = false;
          boolean hasPower = false;

          while (true) {
            stream.mark(1);
            int next = stream.read();

            if (Character.isDigit(next)) {
              ch = next;
              sb.append((char) ch);
              endColumn++;

              if (isScientific) {
                hasPower = true;
              }
            } else if (next == '.') {
              token = Token.LITERAL_FLOAT;

              ch = next;
              sb.append((char) ch);
              endColumn++;
            } else if (next == 'E') {
              token = Token.LITERAL_FLOAT;
              isScientific = true;

              ch = next;
              sb.append((char) ch);
              endColumn++;
            } else if (next == '-') {
              if (isScientific && !hasPower) {
                ch = next;
                sb.append((char) ch);
                endColumn++;
              } else {
                stream.reset();
                break;
              }
            } else {
              stream.reset();
              break;
            }
          }

          String lexeme = sb.toString();
          symbol = new Symbol(token, lexeme, line, column, endColumn);
          column = endColumn;

          break;
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
        }

        if (ch == '"' || ch == '\'') {
          int strChar = ch;

          StringBuilder sb = new StringBuilder();
          int endColumn = column + 1;
          sb.append((char) ch);

          do {
            ch = stream.read();
            endColumn++;
            sb.append((char) ch);

            if (ch == strChar) {
              break;
            } else if (ch == -1) {
              throw LexerException.unclosedString(line, column);
            }
          } while (true);

          String lexeme = sb.toString();

          symbol = new Symbol(Token.LITERAL_STRING, lexeme, line, column, endColumn);
          column = endColumn - 1;

          break;
        }

        if (!Character.isWhitespace(ch)) {
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
        }

        if (ch == '\n' || ch == '\r') {
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
