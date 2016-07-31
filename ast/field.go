package ast

type Field struct {
	Name *Identifier
	Type *Type
}

func (n *Field) Accept(visitor Visitor) {
	visitor.VisitField(n)
}
