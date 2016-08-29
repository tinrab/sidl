package com.moybl.ssdl;

public enum Token {

	EOF,
	IDENTIFIER,
	COMMA,
	ASTERISK,
	OPEN_BRACE,
	CLOSE_BRACE,
	KEYWORD_TYPE,
	KEYWORD_ENUM,
	BRACKETS,

	TYPE_STRING,
	TYPE_INT,
	TYPE_INT8,
	TYPE_INT16,
	TYPE_INT32,
	TYPE_INT64,
	TYPE_UINT,
	TYPE_UINT8,
	TYPE_UINT16,
	TYPE_UINT32,
	TYPE_UINT64,
	TYPE_FLOAT32,
	TYPE_FLOAT64;

	public boolean isType() {
		return ordinal() >= TYPE_STRING.ordinal() && ordinal() <= TYPE_FLOAT64.ordinal();
	}

	@Override
	public String toString() {
		switch (this) {
			case EOF:
				return "EOF";
			case IDENTIFIER:
				return "IDENTIFIER";
			case COMMA:
				return ",";
			case ASTERISK:
				return "*";
			case OPEN_BRACE:
				return "{";
			case CLOSE_BRACE:
				return "}";
			case KEYWORD_TYPE:
				return "type";
			case KEYWORD_ENUM:
				return "enum";
			case BRACKETS:
				return "[]";

			case TYPE_STRING:
				return "s";
			case TYPE_INT:
				return "i";
			case TYPE_INT8:
				return "i8";
			case TYPE_INT16:
				return "i16";
			case TYPE_INT32:
				return "i32";
			case TYPE_INT64:
				return "i64";
			case TYPE_UINT:
				return "u";
			case TYPE_UINT8:
				return "u8";
			case TYPE_UINT16:
				return "u16";
			case TYPE_UINT32:
				return "u32";
			case TYPE_UINT64:
				return "u64";
			case TYPE_FLOAT32:
				return "f32";
			case TYPE_FLOAT64:
				return "f64";
		}

		return super.toString();
	}

}
