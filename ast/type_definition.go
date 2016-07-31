package ast

type TypeDefinition struct {
	Name    *Identifier
	OldName *Identifier
	Type    *Type
	Fields  []*Field
}

func (n *TypeDefinition) Accept(visitor Visitor) {
	visitor.VisitTypeDefinition(n)
}

func (n *TypeDefinition) DefinedName() string {
	return n.Name.Name
}
