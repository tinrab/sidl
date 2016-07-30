package lexer

import (
	"bytes"
	"github.com/paidgeek/ssdl/token"
	"testing"
)

func TestEOF(t *testing.T) {
	b := bytes.NewBufferString("")
	lexer := NewLexer(b)

	assertToken(t, token.EOF, lexer.Next().Token)
}

func TestSymbols(t *testing.T) {
	b := bytes.NewBufferString("*,{}[]")
	lexer := NewLexer(b)

	assertToken(t, token.Asterisk, lexer.Next().Token)
	assertToken(t, token.Comma, lexer.Next().Token)
	assertToken(t, token.OpenBrace, lexer.Next().Token)
	assertToken(t, token.CloseBrace, lexer.Next().Token)
	assertToken(t, token.Brackets, lexer.Next().Token)
	assertToken(t, token.EOF, lexer.Next().Token)
}

func TestWords(t *testing.T) {
	b := bytes.NewBufferString("type enum Name value")
	lexer := NewLexer(b)

	assertToken(t, token.KeywordType, lexer.Next().Token)
	assertToken(t, token.KeywordEnum, lexer.Next().Token)
	assertToken(t, token.Identifier, lexer.Next().Token)
	assertToken(t, token.Identifier, lexer.Next().Token)
	assertToken(t, token.EOF, lexer.Next().Token)
}

func TestTypes(t *testing.T) {
	b := bytes.NewBufferString("string int int8 int16 int32 int64 uint uint8 uint16 uint32 uint64 float32 float64")
	lexer := NewLexer(b)

	assertToken(t, token.TypeString, lexer.Next().Token)
	assertToken(t, token.TypeInt, lexer.Next().Token)
	assertToken(t, token.TypeInt8, lexer.Next().Token)
	assertToken(t, token.TypeInt16, lexer.Next().Token)
	assertToken(t, token.TypeInt32, lexer.Next().Token)
	assertToken(t, token.TypeInt64, lexer.Next().Token)
	assertToken(t, token.TypeUInt, lexer.Next().Token)
	assertToken(t, token.TypeUInt8, lexer.Next().Token)
	assertToken(t, token.TypeUInt16, lexer.Next().Token)
	assertToken(t, token.TypeUInt32, lexer.Next().Token)
	assertToken(t, token.TypeUInt64, lexer.Next().Token)
	assertToken(t, token.TypeFloat32, lexer.Next().Token)
	assertToken(t, token.TypeFloat64, lexer.Next().Token)
	assertToken(t, token.EOF, lexer.Next().Token)
}

func assertToken(t *testing.T, expected token.Token, got token.Token) {
	if expected != got {
		t.Fatalf("%v != %v", expected, got)
		t.Fail()
	}
}
