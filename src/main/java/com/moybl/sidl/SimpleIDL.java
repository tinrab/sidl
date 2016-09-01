package com.moybl.sidl;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;

import com.moybl.sidl.ast.*;
import com.moybl.sidl.semantics.NameChecker;

import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.*;
import java.util.*;

public class SimpleIDL {

	public static void main(String[] args) {
		Option input = new Option("i", "input", true, "input directory");
		input.setRequired(true);
		Option output = new Option("o", "output", true, "output directory");
		output.setRequired(false);
		Option genOneFile = new Option(null, "gen-onefile", false, "Generate single output file");
		genOneFile.setRequired(false);
		Option lang = new Option("l", "lang", true, "Target language");
		lang.setRequired(true);

		Options options = new Options();
		options.addOption(input);
		options.addOption(output);
		options.addOption(genOneFile);
		options.addOption(lang);

		CommandLineParser cmdParser = new DefaultParser();
		HelpFormatter helpFormatter = new HelpFormatter();
		CommandLine cmd;

		try {
			cmd = cmdParser.parse(options, args);
		} catch (ParseException e) {
			System.err.println(e.getMessage());
			return;
		}

		File inputDirectory = new File(cmd.getOptionValue("input"));
		List<Definition> definitions = new ArrayList<Definition>();
		Iterator<File> fileIterator = FileUtils.iterateFiles(inputDirectory, null, true);

		while (fileIterator.hasNext()) {
			File file = fileIterator.next();
			FileInputStream fis = null;

			try {
				fis = new FileInputStream(file);
				Lexer lexer = new Lexer(new BufferedInputStream(fis));
				Parser parser = new Parser(lexer);
				definitions.addAll(parser.parse().getDefinitions());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
					}
				}
			}
		}

		Position p = null;

		if (definitions.size() != 0) {
			Position a = definitions.get(0).getPosition();
			Position b = definitions.get(definitions.size() - 1).getPosition();
			p = Position.expand(a, b);
		}

		Document document = new Document(p, definitions);
		document.accept(new NameChecker());

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

		File outputDirectory = new File(cmd
				.getOptionValue("output", inputDirectory + File.separator + "sidlgenerated"));
		write(outputDirectory, s, cmd.getOptionValue("lang"), cmd.hasOption("gen-onefile"));
	}

	private static void write(File directory, Schema schema, String lang, boolean oneFile) {
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		ve.init();

		StringWriter sw = new StringWriter();
		SchemaUtils utils = new SchemaUtils();

		for (int i = 0; i < schema.getNamespaces().size(); i++) {
			String namespace = schema.getNamespaces().get(i);
			List<Definition> definitions = schema.getDefinitions(namespace);

			for (int j = 0; j < definitions.size(); j++) {
				Definition d = definitions.get(j);
				Template t = null;
				VelocityContext context = new VelocityContext();
				context.put("definition", d);
				context.put("utils", utils);
				context.put("isOneFile", oneFile);
				context.put("path", d.getName().getPath());

				if (d instanceof EnumDefinition) {
					t = ve.getTemplate(lang + "/enum.vm");
				} else {
					t = ve.getTemplate(lang + "/type.vm");
				}

				t.merge(context, sw);

				if(!oneFile){
					Formatter javaFormatter = new Formatter();
					try {
						System.out.println(javaFormatter.formatSource(sw.toString()));
					} catch (FormatterException e) {
						e.printStackTrace();
					}

					sw = new StringWriter();
				}
			}
		}
	}

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
