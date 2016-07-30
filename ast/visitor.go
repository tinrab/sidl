package ast

type Visitor interface {
	VisitDocument(n *Document)
	VisitTypeDefinition(n *TypeDefinition)
	VisitEnumDefinition(n *EnumDefinition)
	VisitField(n *Field)
	VisitType(n *Type)
	VisitIdentifier(n *Identifier)
}
