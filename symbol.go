package main

import (
	"github.com/paidgeek/ssdl/token"
)

type Position struct {
	Line   int
	Column int
}

func NewPosition(line int, column int) Position {
	return Position{
		Line:line,
		Column:column,
	}
}

type Symbol struct {
	Token    token.Token
	Lexeme   string
	Position Position
}

func NewSymbol(token token.Token, lexeme string, line int, column int) Symbol {
	return Symbol{
		Token:token,
		Lexeme:lexeme,
		Position:NewPosition(line, column),
	}
}
