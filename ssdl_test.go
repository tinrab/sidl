package main

import (
	"testing"
	"github.com/stretchr/testify/assert"
	"github.com/paidgeek/ssdl/ast"
	"github.com/paidgeek/ssdl/token"
)

func TestTypeAST(t *testing.T)  {
	assert := assert.New(t)
	document, err := ParseString("type D{} type F{} type A { B int C []*D, E []F }")
	if err != nil {
		t.Fatal(err)
		t.Fail()
	}

	td := document.Definitions[2].(*ast.TypeDefinition)
	assert.Equal("A", td.Name.Name)

	assert.Equal("B", td.Fields[0].Name.Name)
	assert.Equal(token.TypeInt, td.Fields[0].Type.Token)

	assert.Equal("C", td.Fields[1].Name.Name)
	assert.Equal(token.Identifier, td.Fields[1].Type.Token)
	assert.Equal("D", td.Fields[1].Type.Name.Name)
	assert.True(td.Fields[1].Type.IsList)
	assert.True(td.Fields[1].Type.IsReference)

	assert.Equal("E", td.Fields[2].Name.Name)
	assert.Equal(token.Identifier, td.Fields[1].Type.Token)
	assert.Equal("F", td.Fields[2].Type.Name.Name)
	assert.True(td.Fields[2].Type.IsList)
	assert.False(td.Fields[2].Type.IsReference)
}

func TestEnumAST(t *testing.T)  {
	assert := assert.New(t)
	document, _ := ParseString("enum Quality { Common, Epic }")

	ed := document.Definitions[0].(*ast.EnumDefinition)
	assert.Equal("Quality", ed.Name.Name)

	assert.Equal("Common", ed.Values[0].Name)
	assert.Equal("Epic", ed.Values[1].Name)
}

func TestLexerErrorLine(t *testing.T)  {
	assert := assert.New(t)
	_, err := ParseString("type User\n {\n Name string int }")
	assert.EqualError(err, "3:16 expected '}', got 'int'")
}

func TestErrorUndefined(t *testing.T)  {
	assert := assert.New(t)
	_, err := ParseString("type Item { Quality Quality }")
	assert.EqualError(err, "type 'Quality' not defined")
}

func TestInnerType(t *testing.T)  {
	assert := assert.New(t)
	d, _ := ParseString("type Item { Name string, Buff { Attribute int Amount float64 } }")

	td := d.Definitions[0].(*ast.TypeDefinition)
	assert.Equal("Item", td.Name.Name)
	assert.Equal("Name", td.Fields[0].Name.Name)
	assert.Equal(token.TypeString, td.Fields[0].Type.Token)

	assert.Equal("Buff", td.Fields[1].Name.Name)
	assert.True(td.Fields[1].Type.IsInnerType)

	inner := td.Fields[1].Type.Fields
	assert.Equal("Attribute", inner[0].Name.Name)
	assert.Equal(token.TypeInt, inner[0].Type.Token)
	assert.Equal("Amount", inner[1].Name.Name)
	assert.Equal(token.TypeFloat64, inner[1].Type.Token)
}

func TestDisallowInnerReference(t *testing.T)  {
	assert := assert.New(t)
	_, err := ParseString("type Item { Name string, Buff []{} }")
	assert.Nil(err)

	_, err = ParseString("type Item { Name string, Buff []*{} }")
	assert.EqualError(err, "0:43 expected type, got '{'")
}

func TestUndefinedOldName(t *testing.T) {
	assert := assert.New(t)
	_, err := ParseString("type New Old")

	assert.EqualError(err, "type 'Old' not defined")
}
