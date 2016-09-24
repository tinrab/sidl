import com.moybl.sidl.*;
import com.moybl.sidl.ast.*;
import com.moybl.sidl.semantics.SemanticException;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class ParserTest {

  @Test
  public void testEmpty() {
    SimpleIDL.parse("");
  }

  @Test
  public void testTypeAST() {
    Document document = SimpleIDL.parse("class D{} class F{} class A { B i C []D, E []F }");
    ClassDefinition td = (ClassDefinition) document.getDefinitions().get(2);

    Assert.assertEquals("A", td.getName().getSimpleName());

    Assert.assertEquals("B", td.getFields().get(0).getName());
    Assert.assertEquals(Token.TYPE_INT32, ((PrimaryType) td.getFields().get(0).getType())
      .getToken());

    Assert.assertEquals("C", td.getFields().get(1).getName());
    Assert.assertEquals(Token.IDENTIFIER, ((PrimaryType) ((VectorType) td.getFields().get(1)
      .getType()).getType()).getToken());
    Assert.assertEquals("D", ((PrimaryType) ((VectorType) td.getFields().get(1).getType())
      .getType()).getName().getSimpleName());

    Assert.assertEquals("E", td.getFields().get(2).getName());
    Assert.assertEquals(Token.IDENTIFIER, ((PrimaryType) ((VectorType) td.getFields().get(1)
      .getType()).getType())
      .getToken());
    Assert.assertEquals("F", ((PrimaryType) ((VectorType) td.getFields().get(2).getType())
      .getType()).getName().getSimpleName());
  }

  @Test
  public void testInterface() {
    Document document = SimpleIDL.parse("interface I{A i} interface I2 : I{} class T : I2{B i}");
    InterfaceDefinition i = (InterfaceDefinition) document.getDefinitions().get(0);
    InterfaceDefinition i2 = (InterfaceDefinition) document.getDefinitions().get(1);
    ClassDefinition t = (ClassDefinition) document.getDefinitions().get(2);

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

    List<Definition> tpath = t.getParentPath();
    Assert.assertEquals("I2", tpath.get(0).getDefinedName());
    Assert.assertEquals("I", tpath.get(1).getDefinedName());
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
    Document document = SimpleIDL.parse("class A { B [10]b }");
    ClassDefinition td = (ClassDefinition) document.getDefinitions().get(0);

    Assert.assertEquals("A", td.getName().getSimpleName());
    Assert.assertEquals("B", td.getFields().get(0).getName());
    Assert.assertEquals(10, ((ArrayType) td.getFields().get(0).getType()).getLength());
    Assert.assertEquals(Token.TYPE_BOOL, ((PrimaryType) ((ArrayType) td.getFields().get(0)
      .getType()).getType()).getToken());
  }

  @Test
  public void testAttributes() {
    Document document = SimpleIDL
      .parse("@A @B() @C(k1=1, k2='awd', k3=\"ad\", k4 =  3.14) class T{@D f i}");
    ClassDefinition td = (ClassDefinition) document.getDefinitions().get(0);
    Map<String, Attribute> a = td.getAttributes();

    Assert.assertTrue(a.containsKey("A"));
    Assert.assertTrue(a.containsKey("B"));

    Attribute c = a.get("C");
    Assert.assertEquals("C", c.getName());

    Assert.assertEquals("k1", c.getEntries().get("k1").getName());
    Assert.assertEquals(Literal.Kind.INTEGER, c.getEntries().get("k1").getValue().getKind());
    Assert.assertEquals(1, c.getEntries().get("k1").getValue().getLongValue());

    Assert.assertEquals("k2", c.getEntries().get("k2").getName());
    Assert.assertEquals(Literal.Kind.STRING, c.getEntries().get("k2").getValue().getKind());
    Assert.assertEquals("awd", c.getEntries().get("k2").getValue().getStringValue());

    Assert.assertEquals("k3", c.getEntries().get("k3").getName());
    Assert.assertEquals(Literal.Kind.STRING, c.getEntries().get("k3").getValue().getKind());
    Assert.assertEquals("ad", c.getEntries().get("k3").getValue().getStringValue());

    Assert.assertEquals("k4", c.getEntries().get("k4").getName());
    Assert.assertEquals(Literal.Kind.FLOAT, c.getEntries().get("k4").getValue().getKind());
    Assert.assertEquals(3.14, c.getEntries().get("k4").getValue().getDoubleValue(), 0.01);

    Assert.assertEquals("D", td.getFields().get(0).getAttributes().get("D").getName());
  }

  @Test
  public void testUndefined() {
    try {
      SimpleIDL.parse("class Item { Quality Quality }");
    } catch (ParserException e) {
      Assert.assertEquals("[1:23-1:30]: 'Quality' not defined", e.getMessage());

      Position p = e.getPosition();
      Assert.assertEquals(1, p.getStartLine());
      Assert.assertEquals(23, p.getStartColumn());
      Assert.assertEquals(1, p.getEndLine());
      Assert.assertEquals(30, p.getEndColumn());

      return;
    }

    Assert.fail();
  }

  @Test
  public void testDefineNamespace() {
    Document s = SimpleIDL.parse("namespace A.B.C");
    NamespaceDefinition nd = (NamespaceDefinition) s.getDefinitions().get(0);

    Assert.assertEquals("A.B.C", nd.getDefinedName());
  }

  @Test
  public void testServiceDefinition() {
    Document d = SimpleIDL.parse("service S { F(p1 u, p2 f32) i }");
    ServiceDefinition s = (ServiceDefinition) d.getDefinitions().get(0);

    Assert.assertEquals("S", s.getDefinedName());
    Assert.assertEquals("F", s.getFunctions().get(0).getName());
    Assert
      .assertEquals(Token.TYPE_INT32, ((PrimaryType) s.getFunctions().get(0).getType()).getToken());

    List<Parameter> p = s.getFunctions().get(0).getParameters();
    Assert.assertEquals("p1", p.get(0).getName());
    Assert.assertEquals(Token.TYPE_UINT32, ((PrimaryType) p.get(0).getType()).getToken());
    Assert.assertEquals("p2", p.get(1).getName());
    Assert.assertEquals(Token.TYPE_FLOAT32, ((PrimaryType) p.get(1).getType()).getToken());
  }

  @Test
  public void testMapType() {
    Document d = SimpleIDL.parse("struct Value {v i} class T { m <s, Value> }");
    StructDefinition valueDef = (StructDefinition) d.getDefinitions().get(0);
    ClassDefinition tDef = (ClassDefinition) d.getDefinitions().get(1);

    Assert.assertEquals("Value", valueDef.getDefinedName());
    Assert.assertEquals("v", valueDef.getFields().get(0).getName());
    Assert.assertEquals(Token.TYPE_INT32, ((PrimaryType) valueDef.getFields().get(0).getType())
      .getToken());

    Assert.assertEquals("T", tDef.getDefinedName());
    Assert.assertEquals("m", tDef.getFields().get(0).getName());
    MapType m = (MapType) tDef.getFields().get(0).getType();
    Assert.assertEquals(Token.TYPE_STRING, m.getKeyType().getToken());
    Assert.assertEquals("Value", m.getValueType().getName().getSimpleName());
  }

  @Test
  public void testValidStructFieldTypes() {
    SimpleIDL.parse("struct S {a s, v i}");
  }

  @Test(expected = ParserException.class)
  public void testInvalidStructFieldType() {
    SimpleIDL.parse("struct S {a []i}");
  }

  @Test(expected = SemanticException.class)
  public void testIllegalStructFieldClassType() {
    SimpleIDL.parse("class T{} struct S{a T}");
  }

  @Test
  public void testStruct() {
    Document d = SimpleIDL.parse("struct S { x i, y s }");
    StructDefinition s = (StructDefinition) d.getDefinitions().get(0);

    Assert.assertEquals("S", s.getDefinedName());
    Assert.assertEquals("x", s.getFields().get(0).getName());
    Assert
      .assertEquals(Token.TYPE_INT32, ((PrimaryType) s.getFields().get(0).getType()).getToken());
    Assert.assertEquals("y", s.getFields().get(1).getName());
    Assert
      .assertEquals(Token.TYPE_STRING, ((PrimaryType) s.getFields().get(1).getType()).getToken());
  }

}
