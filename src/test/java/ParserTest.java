import com.moybl.sidl.*;
import com.moybl.sidl.ast.*;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ParserTest {

	@Test
	public void testTypeAST() {
		Document document = SimpleIDL.parse("type D{} type F{} type A { B i C []*D, E []F }");
		TypeDefinition td = (TypeDefinition) document.getDefinitions().get(2);

		Assert.assertEquals("A", td.getName().getSimpleName());

		Assert.assertEquals("B", td.getFields().get(0).getName());
		Assert.assertEquals(Token.TYPE_INT32, ((PrimaryType) td.getFields().get(0).getType())
				.getToken());

		Assert.assertEquals("C", td.getFields().get(1).getName());
		Assert.assertEquals(Token.IDENTIFIER, ((PrimaryType) ((ListType) td.getFields().get(1)
				.getType()).getType()).getToken());
		Assert.assertEquals("D", ((PrimaryType) ((ListType) td.getFields().get(1).getType())
				.getType()).getName().getSimpleName());
		Assert.assertTrue(((PrimaryType) ((ListType) td.getFields().get(1).getType()).getType())
				.isReference());

		Assert.assertEquals("E", td.getFields().get(2).getName());
		Assert.assertEquals(Token.IDENTIFIER, ((PrimaryType) ((ListType) td.getFields().get(1)
				.getType()).getType())
				.getToken());
		Assert.assertEquals("F", ((PrimaryType) ((ListType) td.getFields().get(2).getType())
				.getType()).getName().getSimpleName());
		Assert.assertFalse(((PrimaryType) ((ListType) td.getFields().get(2).getType()).getType())
				.isReference());
	}

	@Test
	public void testInterface() {
		Document document = SimpleIDL.parse("interface I{A i} interface I2 : I{} type T : I2{B i}");
		InterfaceDefinition i = (InterfaceDefinition) document.getDefinitions().get(0);
		InterfaceDefinition i2 = (InterfaceDefinition) document.getDefinitions().get(1);
		TypeDefinition t = (TypeDefinition) document.getDefinitions().get(2);

		Assert.assertEquals("I", i.getName().getSimpleName());
		Assert.assertEquals("A", i.getFields().get(0).getName());
		Assert.assertEquals(Token.TYPE_INT32, ((PrimaryType) i.getFields().get(0).getType())
				.getToken());

		Assert.assertEquals("I2", i2.getName().getSimpleName());
		Assert.assertEquals("I", i2.getParentDefinition().getName().getSimpleName());

		Assert.assertEquals("T", t.getName().getSimpleName());
		Assert.assertEquals("I2", t.getParentDefinition().getName().getSimpleName());
		Assert.assertEquals("B", t.getFields().get(0).getName());
		Assert.assertEquals(Token.TYPE_INT32, ((PrimaryType) t.getFields().get(0).getType())
				.getToken());
	}

	@Test
	public void testEnumAST() {
		Document document = SimpleIDL.parse("enum Quality { Common, Epic }");
		EnumDefinition ed = (EnumDefinition) document.getDefinitions().get(0);

		Assert.assertEquals("Quality", ed.getName().getSimpleName());
		Assert.assertEquals("Common", ed.getValues().get(0).getName());
		Assert.assertEquals("Epic", ed.getValues().get(1).getName());
	}

	@Test
	public void testArrayType() {
		Document document = SimpleIDL.parse("type A { B [10]b }");
		TypeDefinition td = (TypeDefinition) document.getDefinitions().get(0);

		Assert.assertEquals("A", td.getName().getSimpleName());
		Assert.assertEquals("B", td.getFields().get(0).getName());
		Assert.assertEquals(10, ((ArrayType) td.getFields().get(0).getType()).getLength());
		Assert.assertEquals(Token.TYPE_BOOL, ((PrimaryType) ((ArrayType) td.getFields().get(0)
				.getType()).getType()).getToken());
	}

	@Test
	public void testAttributes() {
		Document document = SimpleIDL
				.parse("@A @B() @C(k1=1, k2='awd', k3=\"ad\", k4 =  3.14) type T{@D f i}");
		TypeDefinition td = (TypeDefinition) document.getDefinitions().get(0);
		List<Attribute> a = td.getAttributes();

		Assert.assertEquals("A", a.get(0).getName());
		Assert.assertEquals("B", a.get(1).getName());

		Attribute c = a.get(2);
		Assert.assertEquals("C", c.getName());

		Assert.assertEquals("k1", c.getEntries().get(0).getName());
		Assert.assertEquals(Literal.Kind.INTEGER, c.getEntries().get(0).getValue().getKind());
		Assert.assertEquals(1, c.getEntries().get(0).getValue().getLongValue());

		Assert.assertEquals("k2", c.getEntries().get(1).getName());
		Assert.assertEquals(Literal.Kind.STRING, c.getEntries().get(1).getValue().getKind());
		Assert.assertEquals("awd", c.getEntries().get(1).getValue().getStringValue());

		Assert.assertEquals("k3", c.getEntries().get(2).getName());
		Assert.assertEquals(Literal.Kind.STRING, c.getEntries().get(2).getValue().getKind());
		Assert.assertEquals("ad", c.getEntries().get(2).getValue().getStringValue());

		Assert.assertEquals("k4", c.getEntries().get(3).getName());
		Assert.assertEquals(Literal.Kind.FLOAT, c.getEntries().get(3).getValue().getKind());
		Assert.assertEquals(3.14, c.getEntries().get(3).getValue().getDoubleValue(), 0.01);

		Assert.assertEquals("D", td.getFields().get(0).getAttributes().get(0).getName());
	}

	@Test
	public void testUndefined() {
		try {
			SimpleIDL.parse("type Item { Quality Quality }");
		} catch (ParserException e) {
			Assert.assertEquals("'Quality' not defined", e.getMessage());

			Position p = e.getPosition();
			Assert.assertEquals(1, p.getStartLine());
			Assert.assertEquals(22, p.getStartColumn());
			Assert.assertEquals(1, p.getEndLine());
			Assert.assertEquals(29, p.getEndColumn());

			return;
		}

		Assert.fail();
	}

	@Test
	public void testUndefinedOldName() {
		try {
			SimpleIDL.parse("type New Old");
		} catch (ParserException e) {
			Assert.assertEquals("'Old' not defined", e.getMessage());

			Position p = e.getPosition();
			Assert.assertEquals(1, p.getStartLine());
			Assert.assertEquals(10, p.getStartColumn());

			return;
		}

		Assert.fail();
	}

	@Test
	public void testDefineNamespace() {
		Document s = SimpleIDL.parse("namespace A:B:C");
		NamespaceDefinition nd = (NamespaceDefinition) s.getDefinitions().get(0);

		Assert.assertEquals("A:B:C", nd.getDefinedName());
	}

}
