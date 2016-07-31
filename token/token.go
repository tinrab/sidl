package token

import "strconv"

type Token int

const (
	EOF Token = iota
	Identifier
	Comma
	Asterisk
	OpenBrace
	CloseBrace
	KeywordType
	KeywordEnum
	Brackets

	typeBegin
	TypeString
	TypeInt
	TypeInt8
	TypeInt16
	TypeInt32
	TypeInt64
	TypeUInt
	TypeUInt8
	TypeUInt16
	TypeUInt32
	TypeUInt64
	TypeFloat32
	TypeFloat64
	typeEnd
)

var tokens = [...]string{
	EOF: "EOF",
	Identifier: "Identifier",
	Comma:",",
	Asterisk:"*",
	OpenBrace:"{",
	CloseBrace:"}",
	KeywordType:"type",
	KeywordEnum:"enum",
	Brackets:"[]",

	TypeString:"string",
	TypeInt:"int",
	TypeInt8:"int8",
	TypeInt16:"int16",
	TypeInt32:"int32",
	TypeInt64:"int64",
	TypeUInt:"uint",
	TypeUInt8:"uint8",
	TypeUInt16:"uint16",
	TypeUInt32:"uint32",
	TypeUInt64:"uint64",
	TypeFloat32:"float32",
	TypeFloat64:"float64",
}

func (t Token) String() string {
	s := ""
	if 0 <= t && t < Token(len(tokens)) {
		s = tokens[t]
	}
	if s == "" {
		s = "Token(" + strconv.Itoa(int(t)) + ")"
	}
	return s
}

func (t Token) IsType() bool {
	return t > typeBegin && t < typeEnd
}
