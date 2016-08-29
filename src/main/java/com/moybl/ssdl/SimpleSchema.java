package com.moybl.ssdl;

import com.moybl.ssdl.ast.Schema;
import com.moybl.ssdl.semantics.NameChecker;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class SimpleSchema {

	public static Schema parse(InputStream inputStream) {
		Lexer lexer = new Lexer(inputStream);
		Parser parser = new Parser(lexer);
		Schema schema = parser.parse();
		schema.accept(new NameChecker());

		return schema;
	}

	public static Schema parse(String source) {
		return parse(new ByteArrayInputStream(source.getBytes()));
	}

}
