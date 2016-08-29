package com.moybl.ssdl.ast;

import com.moybl.ssdl.Position;

import java.util.List;

public class EnumDefinition extends Definition {

	private Identifier name;
	private List<Identifier> values;

	public EnumDefinition(Position position, Identifier name, List<Identifier> values) {
		super(position);
		this.name = name;
		this.values = values;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public Identifier getName() {
		return name;
	}

	public String getDefinedName() {
		return name.getName();
	}

	public List<Identifier> getValues() {
		return values;
	}

}
