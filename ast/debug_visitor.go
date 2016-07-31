package ast

import (
	"fmt"
	"strings"
)

type DebugVisitor struct {
	ident int
}

func (v *DebugVisitor) VisitDocument(n *Document) {
	for i := 0; i < len(n.Definitions); i++ {
		n.Definitions[i].Accept(v)
	}
}

func (v *DebugVisitor) VisitTypeDefinition(n *TypeDefinition) {
	v.print("type")
	n.Name.Accept(v)
	v.ident++
	for i := 0; i < len(n.Fields); i++ {
		n.Fields[i].Accept(v)
	}
	v.ident--
}

func (v *DebugVisitor) VisitEnumDefinition(n *EnumDefinition) {
	n.Name.Accept(v)
	v.ident++
	for i := 0; i < len(n.Values); i++ {
		n.Values[i].Accept(v)
	}
	v.ident--
}

func (v *DebugVisitor) VisitField(n *Field) {
	n.Name.Accept(v)
	n.Type.Accept(v)
}

func (v *DebugVisitor) VisitType(n *Type) {
	v.print(n.Token)
}

func (v *DebugVisitor) VisitIdentifier(n *Identifier) {
	v.print(n.Name)
}

func (v *DebugVisitor) print(m interface{}) {
	fmt.Printf("%s%v\n", strings.Repeat(" ", v.ident * 3), m)
}
