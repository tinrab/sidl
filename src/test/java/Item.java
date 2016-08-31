import java.io.*;
import java.nio.ByteBuffer;

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

	public void writeTo(ByteBuffer buf) throws IOException {
		Bits.writeString(buf, name);
	}

	private void writeObject(ObjectOutputStream oos) throws IOException {
	}

	public void readFrom(ByteBuffer buf) throws IOException {
		name = Bits.readString(buf);
	}

	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
	}
}
