import com.moybl.ssdl.*;
import com.moybl.ssdl.ast.*;

import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

	@Test
	public void testTypeAST() {
		Schema schema = SimpleSchema.parse("type D{} type F{} type A { B i C []*D, E []F }");
		TypeDefinition td = (TypeDefinition) schema.getDefinitions().get(2);

		Assert.assertEquals("A", td.getName().getName());

		Assert.assertEquals("B", td.getFields().get(0).getName().getName());
		Assert.assertEquals(Token.TYPE_INT, td.getFields().get(0).getType().getToken());

		Assert.assertEquals("C", td.getFields().get(1).getName().getName());
		Assert.assertEquals(Token.IDENTIFIER, td.getFields().get(1).getType().getToken());
		Assert.assertEquals("D", td.getFields().get(1).getType().getName().getName());
		Assert.assertTrue(td.getFields().get(1).getType().isList());
		Assert.assertTrue(td.getFields().get(1).getType().isReference());

		Assert.assertEquals("E", td.getFields().get(2).getName().getName());
		Assert.assertEquals(Token.IDENTIFIER, td.getFields().get(1).getType().getToken());
		Assert.assertEquals("F", td.getFields().get(2).getType().getName().getName());
		Assert.assertTrue(td.getFields().get(2).getType().isList());
		Assert.assertFalse(td.getFields().get(2).getType().isReference());
	}

	@Test
	public void testEnumAST() {
		Schema schema = SimpleSchema.parse("enum Quality { Common, Epic }");
		EnumDefinition ed = (EnumDefinition) schema.getDefinitions().get(0);

		Assert.assertEquals("Quality", ed.getName().getName());
		Assert.assertEquals("Common", ed.getValues().get(0).getName());
		Assert.assertEquals("Epic", ed.getValues().get(1).getName());
	}

	@Test
	public void testUndefined() {
		try {
			SimpleSchema.parse("type Item { Quality Quality }");
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
	public void testDisallowInnerReference() {
		SimpleSchema.parse("type Item { Name s, Buff []{} }");

		try {
			SimpleSchema.parse("type Item { Name s, Buff []*{} }");
		} catch (ParserException e) {
			Assert.assertEquals("Expected type, got '{'", e.getMessage());

			Position p = e.getPosition();
			Assert.assertEquals(1, p.getStartLine());
			Assert.assertEquals(31, p.getStartColumn());

			return;
		}

		Assert.fail();
	}

	@Test
	public void testUndefinedOldName() {
		try {
			SimpleSchema.parse("type New Old");
		} catch (ParserException e) {
			Assert.assertEquals("'Old' not defined", e.getMessage());

			Position p = e.getPosition();
			Assert.assertEquals(1, p.getStartLine());
			Assert.assertEquals(10, p.getStartColumn());

			return;
		}

		Assert.fail();
	}

}
