package ast

import (
	"fmt"
	"strings"
	"github.com/paidgeek/ssdl/token"
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
	v.println("type")
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
	v.ident++
	n.Type.Accept(v)
	v.ident--
}

func (v *DebugVisitor) VisitType(n *Type) {
	str := ""
	if n.IsList {
		str += "[]"
	}
	if n.IsReference {
		str += "*"
	}
	if n.IsInnerType {
		v.ident++
		for _, f := range n.Fields {
			f.Accept(v)
		}
		v.ident--
	} else if n.Token == token.Identifier {
		n.Name.Accept(v)
	} else {
		str += n.Token.String()
	}

	v.println(str)
}

func (v *DebugVisitor) VisitIdentifier(n *Identifier) {
	v.println(n.Name)
}

func (v *DebugVisitor) println(m interface{}) {
	fmt.Printf("%s%v\n", strings.Repeat(" ", v.ident * 3), m)
}
