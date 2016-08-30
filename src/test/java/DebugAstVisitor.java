import com.moybl.sidl.ast.*;

public class DebugAstVisitor implements Visitor {

	private int ident;

	public void visit(Schema node) {
		for (int i = 0; i < node.getNodes().size(); i++) {
			node.getNodes().get(i).accept(this);
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

	public void visit(ArrayType node) {
		print("[%d]", node.getLength());
		node.getType().accept(this);
	}

	public void visit(ListType node) {
		print("[]");
		node.getType().accept(this);
	}

	public void visit(Identifier node) {
		print("Identifier(%s)", node.getName());
	}

	public void visit(EnumValue node) {
		if (node.getValue() != null) {
			print("%s = %s", node.getName().getName(), node.getValue());
		} else {
			print("%s", node.getName().getName());
		}
	}

	public void visit(PrimaryType node) {
		String name = "";

		if (node.getName() != null) {
			name = node.getName().getName();
		} else {
			name = node.getToken().toString();
		}

		print("%s%s", node.isReference() ? "*" : "", name);
	}

	public void visit(Namespace node) {
		print(node.toString());
	}

	public void visit(NamespaceDefinition node) {
		print("namespace %s", node.getDefinedName());
	}

	public void visit(Use node) {
		print("use %s", node.getNamespace().toString());
	}

	private void print(String format, Object... args) {
		for (int i = 0; i < ident * 3; i++) {
			System.out.print(" ");
		}

		System.out.printf(format + "\n", args);
	}

}
