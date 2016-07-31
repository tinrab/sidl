package ast

type EnumDefinition struct {
	Name *Identifier
	Values []*Identifier
}

func (n *EnumDefinition) Accept(visitor Visitor) {
	visitor.VisitEnumDefinition(n)
}

func (n *EnumDefinition) DefinedName() string {
	return n.Name.Name
}
