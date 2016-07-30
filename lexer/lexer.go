package lexer

import (
	"bufio"
	"io"
	"github.com/paidgeek/ssdl/token"
	"github.com/paidgeek/ssdl/util"
	"bytes"
	"fmt"
)

var eof = rune(0)

type Lexer struct {
	r *bufio.Reader
}

func (m *Lexer) Next() (symbol Symbol) {
	symbol.Token = token.EOF
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
				m.scanComment()
			case '*':
				symbol.Token = token.Asterisk
				return symbol
			case ',':
				symbol.Token = token.Comma
				return symbol
			case '{':
				symbol.Token = token.OpenBrace
				return symbol
			case '}':
				symbol.Token = token.CloseBrace
				return symbol
			case '[':
				ch = m.read()
				if ch != ']' {
					panic(fmt.Sprintf("expected ']', got '%c'", ch))
				}

				symbol.Token = token.Brackets
				return symbol
			default:
				panic(fmt.Sprintf("invalid character '%c'", ch))
			}
		}

		ch = m.read()
	}

	return symbol
}

func (m *Lexer) scanComment() {
	ch := m.read()
	for !util.IsNewLine(ch) && ch != eof {
		ch = m.read()
	}
}

func (m *Lexer) scanLowerCaseWord() (symbol Symbol) {
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

	symbol.Lexeme = buf.String()

	switch symbol.Lexeme {
	case "type":
		symbol.Token = token.KeywordType
	case "enum":
		symbol.Token = token.KeywordEnum
	case "string":
		symbol.Token = token.TypeString
	case "int":
		symbol.Token = token.TypeInt
	case "int8":
		symbol.Token = token.TypeInt8
	case "int16":
		symbol.Token = token.TypeInt16
	case "int32":
		symbol.Token = token.TypeInt32
	case "int64":
		symbol.Token = token.TypeInt64
	case "uint":
		symbol.Token = token.TypeUInt
	case "uint8":
		symbol.Token = token.TypeUInt8
	case "uint16":
		symbol.Token = token.TypeUInt16
	case "uint32":
		symbol.Token = token.TypeUInt32
	case "uint64":
		symbol.Token = token.TypeUInt64
	case "float32":
		symbol.Token = token.TypeFloat32
	case "float64":
		symbol.Token = token.TypeFloat64
	default:
		symbol.Token = token.Identifier
	}

	return symbol
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
	return ch
}

func (m *Lexer) unread() {
	m.r.UnreadRune()
}

func NewLexer(r io.Reader) *Lexer {
	return &Lexer{
		r:bufio.NewReader(r),
	}
}