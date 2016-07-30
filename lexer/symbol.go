package lexer

import "github.com/paidgeek/ssdl/token"

type Symbol struct {
	Token token.Token
	Lexeme string
}
