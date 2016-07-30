package ast

type EnumDefinition struct {
	Name *Identifier
	Values []*Identifier
}

func (n *EnumDefinition) Accept(visitor Visitor) {
	visitor.VisitEnumDefinition(n)
}
