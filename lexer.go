package main

import (
	"bufio"
	"io"
	"github.com/paidgeek/ssdl/token"
	"github.com/paidgeek/ssdl/util"
	"bytes"
)

var eof = rune(0)

type Lexer struct {
	r          *bufio.Reader
	wasNewLine bool
	column     int
	line       int
}

func (m *Lexer) Next() Symbol {
	ch := m.read()

	for ch != eof {
		if util.IsLowerCaseLetter(ch) {
			m.unread()
			return m.scanLowerCaseWord()
		} else if util.IsUpperCaseLetter(ch) {
			m.unread()
			return m.scanIdentifier()
		} else if !util.IsWhitespace(ch) {
			switch ch {
			case '#':
				return m.scanComment()
			case '*':
				return m.newSymbol(token.Asterisk)
			case ',':
				return m.newSymbol(token.Comma)
			case '{':
				return m.newSymbol(token.OpenBrace)
			case '}':
				return m.newSymbol(token.CloseBrace)
			case '[':
				ch = m.read()
				if ch != ']' {
					panic(reportf(Position{m.column,m.line}, "expected ']', got '%c'", ch))
				}

				return m.newSymbol(token.Brackets)
			default:
				panic(reportf(Position{m.column,m.line}, "invalid character '%c'", ch))
			}
		} else if util.IsNewLine(ch) && !m.wasNewLine {
			m.wasNewLine = true
			return m.newSymbol(token.NewLine)
		}

		m.wasNewLine = false
		ch = m.read()
	}

	return m.newSymbol(token.EOF)
}

func (m *Lexer) scanComment() Symbol {
	ch := m.read()
	for !util.IsNewLine(ch) && ch != eof {
		ch = m.read()
	}

	t := token.EOF
	lex := ""
	if ch == eof {
		t = token.EOF
	} else if util.IsNewLine(ch) {
		lex = "\n"
		t = token.NewLine
		m.wasNewLine = true
	}

	return m.newSymbolLexeme(t, lex)
}

func (m *Lexer) scanLowerCaseWord() Symbol {
	var buf bytes.Buffer
	buf.WriteRune(m.read())

	for {
		if ch := m.read(); ch == eof {
			break
		} else if !util.IsLowerCaseLetter(ch) && !util.IsDigit(ch) && ch != '_' {
			m.unread()
			break
		} else {
			buf.WriteRune(ch)
		}
	}

	lex := buf.String()
	t := token.EOF

	switch lex {
	case "type":
		t = token.KeywordType
	case "enum":
		t = token.KeywordEnum
	case "string":
		t = token.TypeString
	case "int":
		t = token.TypeInt
	case "int8":
		t = token.TypeInt8
	case "int16":
		t = token.TypeInt16
	case "int32":
		t = token.TypeInt32
	case "int64":
		t = token.TypeInt64
	case "uint":
		t = token.TypeUInt
	case "uint8":
		t = token.TypeUInt8
	case "uint16":
		t = token.TypeUInt16
	case "uint32":
		t = token.TypeUInt32
	case "uint64":
		t = token.TypeUInt64
	case "float32":
		t = token.TypeFloat32
	case "float64":
		t = token.TypeFloat64
	default:
		t = token.Identifier
	}

	return m.newSymbolLexeme(t, lex)
}

func (m *Lexer) scanIdentifier() (symbol Symbol) {
	var buf bytes.Buffer
	buf.WriteRune(m.read())

	for {
		if ch := m.read(); ch == eof {
			break
		} else if !util.IsLetter(ch) && ch != '_' {
			m.unread()
			break
		} else {
			_, _ = buf.WriteRune(ch)
		}
	}

	symbol.Token = token.Identifier
	symbol.Lexeme = buf.String()

	return symbol
}

func (m *Lexer) read() rune {
	ch, _, err := m.r.ReadRune()
	if err != nil {
		return rune(0)
	}

	m.column++
	if util.IsNewLine(ch) {
		m.column = 0
		m.line++
	}

	return ch
}

func (m *Lexer) unread() {
	m.r.UnreadRune()
}

func (m *Lexer) newSymbol(token token.Token) Symbol {
	return NewSymbol(token, token.String(), m.line, m.column)
}

func (m *Lexer) newSymbolLexeme(token token.Token, lexeme string) Symbol {
	return NewSymbol(token, lexeme, m.line, m.column)
}

func NewLexer(r io.Reader) *Lexer {
	return &Lexer{
		r:bufio.NewReader(r),
	}
}
