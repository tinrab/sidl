import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class StreamTest {
	@Test
	public void testSimple() throws Exception {
		Simple simple = new Simple("Hello", 42.0, new Integer[]{1, 2}, Arrays
				.asList(3, 5, 8, 13));

		byte[] buf = new byte[512];
		simple.writeTo(buf, 0);
		simple.reset();
		simple.readFrom(buf, 0);

		Assert.assertEquals("Hello", simple.getName());
		Assert.assertEquals((Double) 42.0, simple.getValue());
		Assert.assertSame(1, simple.getArrAt(0));
		Assert.assertSame(2, simple.getArrAt(1));
		Assert.assertSame(4, simple.getLi().size());
		Assert.assertSame(3, simple.getLiAt(0));
		Assert.assertSame(5, simple.getLiAt(1));
		Assert.assertSame(8, simple.getLiAt(2));
		Assert.assertSame(13, simple.getLiAt(3));
	}

}
