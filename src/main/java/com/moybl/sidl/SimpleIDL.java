package com.moybl.sidl;

import com.moybl.sidl.ast.*;
import com.moybl.sidl.semantics.NameChecker;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SimpleIDL {

	public static Schema compile(InputStream[] inputStreams) {
		List<Definition> definitions = new ArrayList<Definition>();

		for (int i = 0; i < inputStreams.length; i++) {
			Lexer lexer = new Lexer(inputStreams[i]);
			Parser parser = new Parser(lexer);
			definitions.addAll(parser.parse().getDefinitions());
		}

		Position p = null;

		if (definitions.size() != 0) {
			Position a = definitions.get(0).getPosition();
			Position b = definitions.get(definitions.size() - 1).getPosition();
			p = Position.expand(a, b);
		}

		new Document(p, definitions).accept(new NameChecker());
		Schema s = new Schema();
		String ns = "";

		for (int i = 0; i < definitions.size(); i++) {
			Definition d = definitions.get(i);

			if (d instanceof NamespaceDefinition) {
				ns = ((NamespaceDefinition) d).getDefinedName();
			} else {
				s.addDefinition(ns, d);
			}
		}

		return s;
	}

	public static Document parse(InputStream inputStream) {
		Lexer lexer = new Lexer(inputStream);
		Parser parser = new Parser(lexer);
		Document document = parser.parse();
		document.accept(new NameChecker());

		return document;
	}

	public static Document parse(String source) {
		return parse(new ByteArrayInputStream(source.getBytes()));
	}

}
