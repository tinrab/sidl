package main

import (
	"testing"
	"bytes"
)

func TestEnumDefinition(t *testing.T) {
	b := bytes.NewBufferString("enum Quality { Common, Rate\n Epic }")
	lexer := NewLexer(b)
	parser := NewParser(lexer)
	parser.Parse()
}

func TestTypeDefinition(t *testing.T)  {
	b := bytes.NewBufferString("type Item { Name string, Cost uint64 }")
	lexer := NewLexer(b)
	parser := NewParser(lexer)
	parser.Parse()
}
