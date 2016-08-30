package com.moybl.sidl.ast;

import com.moybl.sidl.Position;

import java.util.List;

public class Schema extends Node {

	private List<Node> nodes;

	public Schema(Position position, List<Node> nodes) {
		super(position);
		this.nodes = nodes;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public List<Node> getNodes() {
		return nodes;
	}

}
