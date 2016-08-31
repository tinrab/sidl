import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class StreamTest {

	@Test
	public void testItem() throws Exception {
		Item item = new Item("Apple");
		ByteBuffer bb = ByteBuffer.allocate(1024);

		item.writeTo(bb);

		bb.rewind();

		item.reset();
		item.readFrom(bb);

		Assert.assertEquals("Apple", item.getName());
	}

	@Test
	public void testSimple() throws Exception {
		Item item = new Item("Apple");
		Simple simple = new Simple(item, "Test", 42.0, new Integer[]{1, 2}, Arrays
				.asList(3, 5, 8, 13));

		ByteBuffer bb = ByteBuffer.allocate(1024);
		simple.writeTo(bb);
		bb.rewind();
		simple.reset();
		simple.readFrom(bb);

		Assert.assertEquals("Test", simple.getName());
		Assert.assertEquals((Double) 42.0, simple.getValue());
		Assert.assertSame(1, simple.getArrAt(0));
		Assert.assertSame(2, simple.getArrAt(1));
		Assert.assertSame(4, simple.getLi().size());
		Assert.assertSame(3, simple.getLiAt(0));
		Assert.assertSame(5, simple.getLiAt(1));
		Assert.assertSame(8, simple.getLiAt(2));
		Assert.assertSame(13, simple.getLiAt(3));
		Assert.assertEquals("Apple", simple.getItem().getName());
	}

}
