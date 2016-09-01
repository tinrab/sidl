import java.io.*;


@SuppressWarnings("unused")
public final class Item {
	private String name;

	public Item() {
		reset();
	}

	public Item(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void reset() {
		this.name = "";
	}

	public Item copy() {
		Item newCopy = new Item();
		newCopy.name = this.name;
		return newCopy;
	}

	public int writeTo(byte[] buf, int offset) throws IOException {
		{
			byte[] utf8 = this.name.getBytes("UTF-8");
			int length = utf8.length;
			int i = 0;
			while ((length & 0xFFFFFF80) != 0L) {
				buf[offset + 9 - i++] = ((byte) ((length & 0x7F) | 0x80));
				length >>>= 7;
			}
			buf[offset] = ((byte) (length & 0x7F));
			offset += i + 1;
			System.arraycopy(utf8, 0, buf, offset, utf8.length);
			offset += utf8.length;
		}
		return offset;
	}

	private void writeObject(ObjectOutputStream oos) throws IOException {}

	public int readFrom(byte[] buf, int offset) throws IOException {
		{
			int value = 0;
			int i = 0;
			byte rb = Byte.MIN_VALUE;
			for (; offset < buf.length; offset++) {
				byte b = buf[offset];
				rb = b;
				if ((b & 0x80) == 0) {
					offset++;
					break;
				}
				value |= (b & 0x7f) << i;
				i += 7;
				if (i > 35) {
					throw new RuntimeException("Invalid string length data");
				}
			}
			int length = value | (rb << i);
			byte[] bytes = new byte[length];
			System.arraycopy(buf, offset, bytes, 0, length);
			offset += length;
			this.name = new String(bytes, "UTF-8");
		}
		return offset;
	}

	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {}
}
