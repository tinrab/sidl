package ast

type Field struct {
	Name *Identifier
	Type Node
}

func (n *Field) Accept(visitor Visitor) {
	visitor.VisitField(n)
}
