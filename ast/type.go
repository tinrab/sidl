package ast

import "github.com/paidgeek/ssdl/token"

type Type struct {
	IsInnerType bool
	IsList      bool
	IsReference bool
	Token       token.Token
	Name        *Identifier
	Fields      []*Field
}

func (n *Type) Accept(visitor Visitor) {
	visitor.VisitType(n)
}
