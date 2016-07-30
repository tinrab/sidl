package main

import (
	"testing"
	"bytes"
	"github.com/paidgeek/ssdl/ast"
	"fmt"
	"strings"
)

func TestEnumDefinition(t *testing.T) {
	b := bytes.NewBufferString("enum Quality { Common, Rate\n Epic }")
	lexer := NewLexer(b)
	parser := NewParser(lexer)
	parser.Parse()
}

func TestTypeDefinition(t *testing.T) {
	b := bytes.NewBufferString("type Item { Name string, Cost uint64 }")
	lexer := NewLexer(b)
	parser := NewParser(lexer)
	parser.Parse()
}

func TestBasicAST(t *testing.T) {
	b := bytes.NewBufferString("enum Quality { Common, Rate\n Epic }\ntype Item { Name string, Cost uint64 }")
	lexer := NewLexer(b)
	parser := NewParser(lexer)
	parser.Parse().Accept(&DebugVisitor{})
}

type DebugVisitor struct {
	ident int
}

func (v *DebugVisitor) VisitDocument(n *ast.Document) {
	for i := 0; i < len(n.Definitions); i++ {
		n.Definitions[i].Accept(v)
	}
}

func (v *DebugVisitor) VisitTypeDefinition(n *ast.TypeDefinition) {
	n.Name.Accept(v)
	v.ident++
	for i := 0; i < len(n.Fields); i++ {
		n.Fields[i].Accept(v)
	}
	v.ident--
}

func (v *DebugVisitor) VisitEnumDefinition(n *ast.EnumDefinition) {
	n.Name.Accept(v)
	v.ident++
	for i := 0; i < len(n.Values); i++ {
		n.Values[i].Accept(v)
	}
	v.ident--
}

func (v *DebugVisitor) VisitField(n *ast.Field) {
	n.Name.Accept(v)
	n.Type.Accept(v)
}

func (v *DebugVisitor) VisitType(n *ast.Type) {
	if n.Name == nil {
		v.print(n.Token)
	} else {
		v.print(n.Name)
	}
}

func (v *DebugVisitor) VisitIdentifier(n *ast.Identifier) {
	v.print(n.Name)
}

func (v *DebugVisitor) print(m interface{}) {
	fmt.Printf("%s%v\n", strings.Repeat(" ", v.ident * 3), m)
}
