import java.io.*;
import java.util.ArrayList;

@SuppressWarnings("unused")
public final class Simple {
	private String name;
	private Double value;
	private Integer[] arr;
	public static final int ARR_LENGTH = 10;
	private java.util.List<Integer> li;

	public Simple() {
		reset();
	}

	public Simple(String name, Double value, Integer[] arr, java.util.List<Integer> li) {
		this.name = name;
		this.value = value;
		this.arr = arr;
		this.li = li;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Integer[] getArr() {
		return arr;
	}

	public void setArr(Integer[] arr) {
		this.arr = arr;
	}

	public Integer getArrAt(int index) {
		return this.arr[index];
	}

	public void setArrAt(int index, Integer value) {
		this.arr[index] = value;
	}

	public java.util.List<Integer> getLi() {
		return li;
	}

	public void setLi(java.util.List<Integer> li) {
		this.li = li;
	}

	public Integer getLiAt(int index) {
		return this.li.get(index);
	}

	public void setLiAt(int index, Integer value) {
		this.li.set(index, value);
	}

	public void reset() {
		this.name = "";
		this.value = 0.0;
		this.arr = new Integer[ARR_LENGTH];
		this.li = new ArrayList<Integer>();
	}

	public int writeTo(byte[] buf, int offset) throws IOException {
		{
			byte[] utf8 = this.name.getBytes("UTF-8");
			int length = utf8.length;
			int i = 0;
			while ((length & 0xFFFFFF80) != 0L) {
				buf[offset + 9 - i++] =  ((byte) ((length & 0x7F) | 0x80));
				length >>>= 7;
			}
			buf[offset] = ((byte)(length & 0x7F));
			offset += i + 1;
			System.arraycopy(utf8, 0, buf, offset, utf8.length);
			offset += utf8.length;
		}
		{

		}
		return offset;
	}

	private void writeObject(ObjectOutputStream oos) throws IOException {
	}

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
					throw new IllegalArgumentException("Variable length quantity is too long");
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

	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
	}
}
