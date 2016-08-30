package com.moybl.sidl.ast;

import com.moybl.sidl.Token;

public class BaseType {

	private Identifier name;
	private Token token;
	private boolean isReference;

	public BaseType(Identifier name, boolean isReference) {
		this.name = name;
		this.isReference = isReference;
	}

	public BaseType(Token token, boolean isReference) {
		this.token = token;
		this.isReference = isReference;
	}

	public Identifier getName() {
		return name;
	}

	public Token getToken() {
		return token;
	}

	public boolean isReference() {
		return isReference;
	}

}
