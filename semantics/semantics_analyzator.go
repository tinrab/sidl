package semantics

import (
	"github.com/paidgeek/ssdl/ast"
	"github.com/paidgeek/ssdl/token"
	"fmt"
)

type semanticsAnalyzator struct {
	names map[string]ast.Definition
}

func (v *semanticsAnalyzator) VisitDocument(n *ast.Document) {
	for _, def := range n.Definitions {
		v.names[def.DefinedName()] = def
	}

	for _, def := range n.Definitions {
		def.Accept(v)
	}
}

func (v *semanticsAnalyzator) VisitTypeDefinition(n *ast.TypeDefinition) {
	if n.OldName != nil {
		n.OldName.Accept(v)
	} else {
		for _, f := range n.Fields {
			f.Accept(v)
		}
	}
}

func (v *semanticsAnalyzator) VisitEnumDefinition(n *ast.EnumDefinition) {}

func (v *semanticsAnalyzator) VisitField(n *ast.Field) {
	n.Type.Accept(v)
}

func (v *semanticsAnalyzator) VisitType(n *ast.Type) {
	if n.Token == token.Identifier {
		n.Name.Accept(v)
	}
}

func (v *semanticsAnalyzator) VisitIdentifier(n *ast.Identifier) {
	if _, ok := v.names[n.Name]; !ok {
		panic(fmt.Sprintf("type '%s' not defined", n.Name))
	}
}

func NewSemanticsAnalyzator() *semanticsAnalyzator {
	return &semanticsAnalyzator{
		names:make(map[string]ast.Definition),
	}
}
