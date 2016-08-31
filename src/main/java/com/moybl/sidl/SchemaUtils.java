package com.moybl.sidl;

public class SchemaUtils {

	public String toUpperCase(String s) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);

			if (i > 0 && Character.isUpperCase(ch)) {
				sb.append("_");
			}

			sb.append(Character.toUpperCase(ch));
		}

		return sb.toString();
	}

	public String toCamelCase(String s) {
		return Character.toLowerCase(s.charAt(0)) + s.substring(1);
	}

}
