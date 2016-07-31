package main

import (
	"github.com/paidgeek/ssdl/token"
	"github.com/paidgeek/ssdl/ast"
)

type Parser struct {
	lexer   *Lexer
	current Symbol
	next    Symbol
}

func (p *Parser) Parse() *ast.Document {
	p.next = p.lexer.Next()
	definitions := make([]ast.Definition, 0)

	for !p.match(token.EOF) {
		definitions = append(definitions, p.parseDefinition())
	}

	return &ast.Document{
		Definitions:definitions,
	}
}

func (p *Parser) parseDefinition() ast.Definition {
	switch p.peek() {
	case token.KeywordType:
		return p.parseTypeDefinition()
	case token.KeywordEnum:
		return p.parseEnumDefinition()
	}

	panic(reportf(p.current.Position, "unexpected '%v'", p.next.Lexeme))
	return nil
}

func (p *Parser) parseTypeDefinition() *ast.TypeDefinition {
	p.check(token.KeywordType)
	name := p.parseIdentifier()
	fields := p.parseFieldList()

	return &ast.TypeDefinition{
		Name:name,
		Fields:fields,
	}
}

func (p *Parser) parseEnumDefinition() *ast.EnumDefinition {
	p.check(token.KeywordEnum)
	name := p.parseIdentifier()
	values := p.parseIdentifierList()

	return &ast.EnumDefinition{
		Name:name,
		Values:values,
	}
}

func (p *Parser) parseFieldList() []*ast.Field {
	p.check(token.OpenBrace)
	fields := make([]*ast.Field, 0)

	for p.match(token.Identifier) {
		fields = append(fields, p.parseField())
		p.accept(token.Comma)
	}
	p.check(token.CloseBrace)

	return fields
}

func (p *Parser) parseIdentifierList() []*ast.Identifier {
	p.check(token.OpenBrace)
	identifiers := make([]*ast.Identifier, 0)

	for p.match(token.Identifier) {
		identifiers = append(identifiers, p.parseIdentifier())
		p.accept(token.Comma)
	}
	p.check(token.CloseBrace)

	return identifiers
}

func (p *Parser) parseField() *ast.Field {
	name := p.parseIdentifier()
	t := p.parseType()

	return &ast.Field{
		Name:name,
		Type:t,
	}
}

func (p *Parser) parseIdentifier() *ast.Identifier {
	p.check(token.Identifier)
	return &ast.Identifier{
		Name:p.current.Lexeme,
	}
}

func (p *Parser) parseType() *ast.Type {
	isList := p.accept(token.Brackets)

	if p.match(token.OpenBrace) {
		fields := p.parseFieldList()

		return &ast.Type{
			IsInnerType:true,
			Fields:fields,
		}
	}

	isReference := p.accept(token.Asterisk)

	if p.match(token.Identifier) {
		return &ast.Type{
			Name:p.parseIdentifier(),
			Token:token.Identifier,
			IsList:isList,
			IsReference:isReference,
		}
	} else {
		if !p.peek().IsType() {
			panic(reportf(p.current.Position, "expected type, got '%v'", p.next.Token))
		}

		p.accept(p.peek())

		return &ast.Type{
			Token:p.current.Token,
			IsList:isList,
			IsReference:isReference,
		}
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
