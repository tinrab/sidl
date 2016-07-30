package main

import (
	"github.com/paidgeek/ssdl/token"
)

type Parser struct {
	lexer   *Lexer
	current Symbol
	next    Symbol
}

func (p *Parser) Parse() {
	p.next = p.lexer.Next()

	for !p.match(token.EOF) {
		p.parseDefinition()
	}
}

func (p *Parser) parseDefinition() {
	switch p.peek() {
	case token.KeywordType:
		p.parseTypeDefinition()
	case token.KeywordEnum:
		p.parseEnumDefinition()
	}
}

func (p *Parser) parseTypeDefinition() {
	p.check(token.KeywordType, token.Identifier, token.OpenBrace)
	p.parseFieldList()
	p.check(token.CloseBrace)
}

func (p *Parser) parseEnumDefinition() {
	p.check(token.KeywordEnum, token.Identifier, token.OpenBrace)
	p.parseIdentifierList()
	p.check(token.CloseBrace)
}

func (p *Parser) parseFieldList() {
	for p.match(token.Identifier) {
		p.parseField()

		if !p.accept(token.NewLine, token.Comma) {
			break
		}
	}
}

func (p *Parser) parseIdentifierList() {
	for p.accept(token.Identifier) {
		if !p.accept(token.NewLine, token.Comma) {
			break
		}
	}
}

func (p *Parser) parseField() {
	p.check(token.Identifier)

	if p.peek().IsType() {
		p.accept(p.peek())
	} else {
		panic(reportf(p.current.Position, "invalid type"))
	}
}

func (p *Parser) peek() token.Token {
	return p.next.Token
}

func (p *Parser) match(token token.Token) bool {
	return p.peek() == token
}

func (p *Parser) check(tokens ...token.Token) {
	for _, t := range tokens {
		if p.match(t) {
			p.current = p.next
			p.next = p.lexer.Next()
		} else {
			panic(reportf(p.current.Position, "expected '%v', got '%v'", t, p.next.Token))
		}
	}
}

func (p *Parser) accept(tokens ...token.Token) bool {
	for _, t := range tokens {
		if p.match(t) {
			p.current = p.next
			p.next = p.lexer.Next()
			return true
		}
	}
	return false
}

func NewParser(lexer *Lexer) *Parser {
	return &Parser{
		lexer:lexer,
	}
}
