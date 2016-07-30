package ast

type Identifier struct {
	Name string
}

func (n *Identifier) Accept(visitor Visitor) {
	visitor.VisitIdentifier(n)
}
