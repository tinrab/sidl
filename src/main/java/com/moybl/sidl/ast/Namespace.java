package com.moybl.sidl.ast;

import com.moybl.sidl.Position;

import java.util.List;

public class Namespace extends Node {

	private List<Identifier> path;

	public Namespace(Position position, List<Identifier> path) {
		super(position);
		this.path = path;
	}

	public List<Identifier> getPath() {
		return path;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < path.size(); i++) {
			sb.append(path.get(i).getName());

			if (i < path.size() - 1) {
				sb.append(":");
			}
		}

		return sb.toString();
	}

}
