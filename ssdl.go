package main

import (
	"github.com/paidgeek/ssdl/ast"
	"github.com/paidgeek/ssdl/semantics"
	"bytes"
	"errors"
	"io"
	"fmt"
)

func ParseString(source string) (*ast.Document, error) {
	return Parse(bytes.NewBufferString(source))
}

func Parse(r io.Reader) (document *ast.Document, err error) {
	defer func() {
		if r := recover(); r != nil {
			document = nil
			err = errors.New(fmt.Sprintf("%v", r))
		}
	}()

	lexer := NewLexer(r)
	parser := NewParser(lexer)

	document = parser.Parse()
	document.Accept(semantics.NewSemanticsAnalyzator())

	return document, nil
}
