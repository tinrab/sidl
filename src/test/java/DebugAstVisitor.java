import com.moybl.ssdl.Token;
import com.moybl.ssdl.ast.*;

public class DebugAstVisitor implements Visitor {

	private int ident;

	public void visit(Schema node) {
		for (int i = 0; i < node.getDefinitions().size(); i++) {
			node.getDefinitions().get(i).accept(this);
		}
	}

	public void visit(TypeDefinition node) {
		print("type");
		node.getName().accept(this);

		ident++;
		for (int i = 0; i < node.getFields().size(); i++) {
			node.getFields().get(i).accept(this);
		}
		ident--;
	}

	public void visit(EnumDefinition node) {
		node.getName().accept(this);

		ident++;
		for (int i = 0; i < node.getValues().size(); i++) {
			node.getValues().get(i).accept(this);
		}
		ident--;
	}

	public void visit(Field node) {
		node.getName().accept(this);
		ident++;
		node.getType().accept(this);
		ident--;
	}

	public void visit(Type node) {
		StringBuilder sb = new StringBuilder();

		if (node.isList()) {
			sb.append("[]");
		}

		if (node.isReference()) {
			sb.append("*");
		}

		if (node.isInnerType()) {
			ident++;
			for (int i = 0; i < node.getFields().size(); i++) {
				node.getFields().get(i).accept(this);
			}
			ident--;
		} else if (node.getToken() == Token.IDENTIFIER) {
			node.getName().accept(this);
		} else {
			sb.append(node.getToken().toString());
		}

		System.out.println(sb.toString());
	}

	public void visit(Identifier node) {
		print("Identifier(%s)", node.getName());
	}

	private void print(String format, Object... args) {
		for (int i = 0; i < ident * 3; i++) {
			System.out.print(" ");
		}

		System.out.printf(format + "\n", args);
	}

}
