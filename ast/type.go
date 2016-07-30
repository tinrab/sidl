package ast

import "github.com/paidgeek/ssdl/token"

type Type struct {
	Token token.Token
	Name  *Identifier
}

func (n *Type) Accept(visitor Visitor) {
	visitor.VisitType(n)
}
