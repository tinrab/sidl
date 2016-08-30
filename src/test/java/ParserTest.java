import com.moybl.sidl.*;
import com.moybl.sidl.ast.*;

import org.junit.Assert;
import org.junit.Test;

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
