package com.moybl.sidl;

public class Position {

  private int startLine;
  private int endLine;
  private int startColumn;
  private int endColumn;

  public Position(int startLine, int endLine, int startColumn, int endColumn) {
    this.startLine = startLine;
    this.endLine = endLine;
    this.startColumn = startColumn;
    this.endColumn = endColumn;
  }

  public Position(int line, int column) {
    this(line, line, column, column);
  }

  public Position(Position start, Position end) {
    startLine = start.getStartLine();
    endLine = end.getEndLine();
    startColumn = start.getStartColumn();
    endColumn = end.getEndColumn();
  }

  public int getStartLine() {
    return startLine;
  }

  public int getEndLine() {
    return endLine;
  }

  public int getStartColumn() {
    return startColumn;
  }

  public int getEndColumn() {
    return endColumn;
  }

  @Override
  public String toString() {
    return String.format("[%d:%d-%d:%d]", startLine, startColumn, endLine, endColumn);
  }

  public static Position expand(Position a, Position b) {
    return new Position(a.startLine, b.endLine, a.startColumn, b.endColumn);
  }

}
