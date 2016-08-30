package com.moybl.sidl.ast;

import com.moybl.sidl.Position;

import java.util.ArrayList;
import java.util.List;

public class Identifier extends Node {

	private List<String> path;

	public Identifier(Position position, List<String> path) {
		super(position);
		this.path = path;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public List<String> getPath() {
		return path;
	}

	public String getCanonicalName() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < path.size(); i++) {
			sb.append(path.get(i));

			if (i < path.size() - 1) {
				sb.append(":");
			}
		}

		return sb.toString();
	}

	public String getSimpleName() {
		return path.get(path.size() - 1);
	}

}
