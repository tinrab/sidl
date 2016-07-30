package ast

type TypeDefinition struct {
	Name   *Identifier
	Fields []*Field
}

func (n *TypeDefinition) Accept(visitor Visitor) {
	visitor.VisitTypeDefinition(n)
}
