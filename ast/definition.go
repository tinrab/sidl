package ast

type Definition interface {
	Node
	DefinedName() string
}
