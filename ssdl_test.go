package main

import (
	"testing"
	"github.com/stretchr/testify/assert"
	"github.com/paidgeek/ssdl/ast"
	"github.com/paidgeek/ssdl/token"
)

func TestTypeAST(t *testing.T)  {
	assert := assert.New(t)
	document, _ := ParseString("type User { Name string, Age int }")

	td := document.Definitions[0].(*ast.TypeDefinition)
	assert.Equal("User", td.Name.Name)

	assert.Equal("Name", td.Fields[0].Name.Name)
	assert.Equal(token.TypeString, td.Fields[0].Type.Token)
	assert.Equal("Age", td.Fields[1].Name.Name)
	assert.Equal(token.TypeInt, td.Fields[1].Type.Token)
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
