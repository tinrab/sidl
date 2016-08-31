import java.io.IOException;
import java.nio.ByteBuffer;

public class Bits {

	public static void writeString(ByteBuffer buf, String value) throws IOException {
		writeUnsignedVarInt(buf, value.length());
		buf.put(value.getBytes("UTF-8"));
	}

	public static String readString(ByteBuffer buf) throws IOException {
		int length = Bits.readUnsignedVarInt(buf);
		byte[] nameBytes = new byte[length];
		buf.get(nameBytes);
		return new String(nameBytes, "UTF-8");
	}

	public static void writeSignedVarInt(ByteBuffer buf, int value) {
		writeUnsignedVarInt(buf, (value << 1) ^ (value >> 31));
	}

	public static void writeUnsignedVarInt(ByteBuffer buf, int value) {
		byte[] byteArrayList = new byte[10];
		int i = 0;
		while ((value & 0xFFFFFF80) != 0L) {
			byteArrayList[i++] = ((byte) ((value & 0x7F) | 0x80));
			value >>>= 7;
		}
		byteArrayList[i] = ((byte) (value & 0x7F));
		for (; i >= 0; i--) {
			buf.put(byteArrayList[i]);
		}
	}

	public static int readSignedVarInt(ByteBuffer buf) {
		int raw = readUnsignedVarInt(buf);
		int temp = (((raw << 31) >> 31) ^ raw) >> 1;
		return temp ^ (raw & (1 << 31));
	}

	public static int readUnsignedVarInt(ByteBuffer buf) {
		int value = 0;
		int i = 0;
		byte rb = Byte.MIN_VALUE;
		while(buf.hasRemaining()) {
			byte b = buf.get();
			rb = b;
			if ((b & 0x80) == 0) {
				break;
			}
			value |= (b & 0x7f) << i;
			i += 7;
			if (i > 35) {
				throw new IllegalArgumentException("Variable length quantity is too long");
			}
		}
		return value | (rb << i);
	}

}
