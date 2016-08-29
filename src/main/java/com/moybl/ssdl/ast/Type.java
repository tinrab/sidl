package com.moybl.ssdl.ast;

import com.moybl.ssdl.Position;
import com.moybl.ssdl.Token;

import java.util.List;

public class Type extends Node {

	private boolean isInnerType;
	private boolean isList;
	private boolean isReference;
	private Token token;
	private Identifier name;
	private List<Field> fields;

	public Type(Position position, List<Field> fields) {
		super(position);
		this.fields = fields;
		isInnerType = true;
	}

	public Type(Position position, Identifier name, boolean isList, boolean isReference) {
		super(position);
		this.name = name;
		this.isList = isList;
		this.isReference = isReference;
		token = Token.IDENTIFIER;
	}

	public Type(Position position, boolean isList, boolean isReference, Token token) {
		super(position);
		this.token = token;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public boolean isInnerType() {
		return isInnerType;
	}

	public boolean isList() {
		return isList;
	}

	public boolean isReference() {
		return isReference;
	}

	public Token getToken() {
		return token;
	}

	public Identifier getName() {
		return name;
	}

	public List<Field> getFields() {
		return fields;
	}

}
