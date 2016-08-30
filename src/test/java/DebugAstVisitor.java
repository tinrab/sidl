import com.moybl.sidl.ast.*;

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

	@Override
	public void visit(ArrayType node) {
		String name = null;

		if (node.getType().getName() != null) {
			name = node.getType().getName().getName();
		} else {
			name = node.getType().getToken().toString();
		}

		print("[%d]%s%s", node.getLength(), node.getType().isReference() ? "*" : "", name);
	}

	@Override
	public void visit(ListType node) {
		String name = null;

		if (node.getType().getName() != null) {
			name = node.getType().getName().getName();
		} else {
			name = node.getType().getToken().toString();
		}

		print("[]%s%s", node.getType().isReference() ? "*" : "", name);

	}

	public void visit(Identifier node) {
		print("Identifier(%s)", node.getName());
	}

	@Override
	public void visit(EnumValue node) {
		if (node.getValue() != null) {
			print("%s = %s", node.getName().getName(), node.getValue());
		} else {
			print("%s", node.getName().getName());
		}
	}

	private void print(String format, Object... args) {
		for (int i = 0; i < ident * 3; i++) {
			System.out.print(" ");
		}

		System.out.printf(format + "\n", args);
	}

}
