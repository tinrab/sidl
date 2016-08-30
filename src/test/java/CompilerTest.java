import com.moybl.sidl.*;
import com.moybl.sidl.ast.*;

import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

public class CompilerTest {

	@Test
	public void testRPG() throws Exception {
		Schema s = SimpleIDL.compile(new InputStream[]{
				getClass().getResourceAsStream("rpg.sidl"),
				getClass().getResourceAsStream("rpg_inventory.sidl"),
		});

		List<Definition> rpg = s.getDefinitions("RPG");
		List<Definition> inv = s.getDefinitions("RPG:Inventory");

		TypeDefinition character = (TypeDefinition) rpg.get(0);
		EnumDefinition quality = (EnumDefinition) inv.get(0);
		TypeDefinition item = (TypeDefinition) inv.get(1);
		TypeDefinition inventory = (TypeDefinition) inv.get(2);

		Assert.assertEquals("RPG:Character", character.getDefinedName());
		Assert.assertEquals("Name", character.getFields().get(0).getName());
		Assert.assertEquals(Token.TYPE_STRING, ((PrimaryType) character.getFields().get(0).getType())
				.getToken());
		Assert.assertEquals("Speed", character.getFields().get(1).getName());
		Assert.assertEquals(Token.TYPE_FLOAT32, ((PrimaryType) character.getFields().get(1).getType())
				.getToken());
		Assert.assertEquals("Bag", character.getFields().get(2).getName());
		Assert.assertEquals("RPG:Inventory:Inventory", ((PrimaryType) character.getFields().get(2)
				.getType())
				.getName().getCanonicalName());
		Assert.assertEquals("MainHand", character.getFields().get(3).getName());
		Assert.assertEquals("RPG:Inventory:Item", ((PrimaryType) character.getFields().get(3)
				.getType())
				.getName().getCanonicalName());
		Assert.assertEquals("Buffs", character.getFields().get(4).getName());
		Assert.assertEquals(8, ((ArrayType) character.getFields().get(4).getType()).getLength());
		Assert.assertEquals(Token.TYPE_FLOAT64, ((PrimaryType) ((ArrayType) character.getFields()
				.get(4).getType()).getType()).getToken());

		Assert.assertEquals("RPG:Inventory:Quality", quality.getDefinedName());
		Assert.assertEquals("Common", quality.getValues().get(0).getName());
		Assert.assertEquals("Rare", quality.getValues().get(1).getName());
		Assert.assertEquals("Epic", quality.getValues().get(2).getName());

		Assert.assertEquals("RPG:Inventory:Item", item.getDefinedName());
		Assert.assertEquals("Name", item.getFields().get(0).getName());
		Assert.assertEquals(Token.TYPE_STRING, ((PrimaryType) item.getFields().get(0).getType())
				.getToken());
		Assert.assertEquals("Quality", item.getFields().get(1).getName());
		Assert.assertEquals("RPG:Inventory:Quality", ((PrimaryType) item.getFields().get(1)
				.getType())
				.getName().getCanonicalName());
		Assert.assertEquals("Cost", item.getFields().get(2).getName());
		Assert.assertEquals(Token.TYPE_UINT64, ((PrimaryType) item.getFields().get(2)
				.getType()).getToken());

		Assert.assertEquals("RPG:Inventory:Inventory", inventory.getDefinedName());
		Assert.assertEquals("Capacity", inventory.getFields().get(0).getName());
		Assert.assertEquals(Token.TYPE_UINT32, ((PrimaryType) inventory.getFields().get(0).getType())
				.getToken());
		Assert.assertEquals("Items", inventory.getFields().get(1).getName());
		Assert.assertTrue(((PrimaryType) ((ListType) inventory.getFields().get(1).getType())
				.getType()).isReference());
	}

}
