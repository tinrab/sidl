package com.moybl.sidl;

public enum Token {

	EOF,
	IDENTIFIER,
	COMMA,
	COLON,
	EQUALS,
	ASTERISK,
	OPEN_BRACE,
	CLOSE_BRACE,
	OPEN_BRACKET,
	CLOSE_BRACKET,
	KEYWORD_TYPE,
	KEYWORD_ENUM,
	KEYWORD_NAMESPACE,
	LITERAL_INTEGER,

	TYPE_STRING,
	TYPE_BOOL,
	TYPE_INT8,
	TYPE_INT16,
	TYPE_INT32,
	TYPE_INT64,
	TYPE_UINT8,
	TYPE_UINT16,
	TYPE_UINT32,
	TYPE_UINT64,
	TYPE_FLOAT32,
	TYPE_FLOAT64;

	public boolean isType() {
		return ordinal() >= TYPE_STRING.ordinal() && ordinal() <= TYPE_FLOAT64.ordinal();
	}

	public boolean isIntegerType() {
		return ordinal() >= TYPE_INT8.ordinal() && ordinal() <= TYPE_UINT64.ordinal();
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
			case COLON:
				return ":";
			case OPEN_BRACE:
				return "{";
			case EQUALS:
				return "=";
			case CLOSE_BRACE:
				return "}";
			case OPEN_BRACKET:
				return "[";
			case CLOSE_BRACKET:
				return "]";
			case KEYWORD_TYPE:
				return "type";
			case KEYWORD_ENUM:
				return "enum";
			case KEYWORD_NAMESPACE:
				return "namespace";
			case LITERAL_INTEGER:
				return "LITERAL_INTEGER";

			case TYPE_STRING:
				return "s";
			case TYPE_BOOL:
				return "b";
			case TYPE_INT8:
				return "i8";
			case TYPE_INT16:
				return "i16";
			case TYPE_INT32:
				return "i32";
			case TYPE_INT64:
				return "i64";
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
