import java.io.*;

@SuppressWarnings("unused")
public final class Simple {
	private Item item;
	private String name;
	private Double value;
	private Integer[] arr;
	public static final int ARR_LENGTH = 2;
	private java.util.List<Integer> li;

	public Simple() {
		reset();
	}

	public Simple(Item item, String name, Double value, Integer[] arr, java.util.List<Integer> li) {
		this.item = item;
		this.name = name;
		this.value = value;
		this.arr = arr;
		this.li = li;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
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
		this.item = null;
		this.name = "";
		this.value = 0.0;
		this.arr = new Integer[ARR_LENGTH];
		this.li = new java.util.ArrayList<Integer>();
	}

	public Simple copy() {
		Simple newCopy = new Simple();
		if (this.item != null) {
			newCopy.item = this.item.copy();
		}
		newCopy.name = this.name;
		newCopy.value = this.value;
		newCopy.arr = new Integer[ARR_LENGTH];
		for (int i = 0; i < ARR_LENGTH; i++) {
			newCopy.arr[i] = this.arr[i];
		}
		newCopy.li = new java.util.ArrayList<Integer>(this.li.size());
		for (int i = 0; i < this.li.size(); i++) {
			newCopy.li.add(this.li.get(i));
		}
		return newCopy;
	}

	public int writeTo(byte[] buf, int offset) throws IOException {
		{
			if (this.item == null) {
				buf[offset++] = (byte) 0x80;
			} else {
				buf[offset++] = (byte) 0x81;
				offset = this.item.writeTo(buf, offset);
			}
		}
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
		{
			long value = Double.doubleToRawLongBits(this.value);
			buf[offset++] = (byte) value;
			buf[offset++] = (byte) (value >> 8);
			buf[offset++] = (byte) (value >> 16);
			buf[offset++] = (byte) (value >> 24);
			buf[offset++] = (byte) (value >> 32);
			buf[offset++] = (byte) (value >> 40);
			buf[offset++] = (byte) (value >> 48);
			buf[offset++] = (byte) (value >> 56);
		}
		{
			for (int i = 0; i < ARR_LENGTH; i++) {
				int el = this.arr[i];
				buf[offset++] = (byte) el;
				buf[offset++] = (byte) (el >> 8);
				buf[offset++] = (byte) (el >> 16);
				buf[offset++] = (byte) (el >> 24);
			}
		}
		{
			{
				int length = this.li.size();
				int i = 0;
				while ((length & 0xFFFFFF80) != 0L) {
					buf[offset + 9 - i++] = ((byte) ((length & 0x7F) | 0x80));
					length >>>= 7;
				}
				buf[offset] = ((byte) (length & 0x7F));
				offset += i + 1;
			}
			for (int i = 0; i < this.li.size(); i++) {
				int el = this.li.get(i);
				buf[offset++] = (byte) el;
				buf[offset++] = (byte) (el >> 8);
				buf[offset++] = (byte) (el >> 16);
				buf[offset++] = (byte) (el >> 24);
			}
		}
		return offset;
	}

	private void writeObject(ObjectOutputStream oos) throws IOException {}

	public int readFrom(byte[] buf, int offset) throws IOException {
		{
			if (buf[offset++] == (byte) 0x81) {
				if (this.item == null) {
					this.item = new Item();
				}
				offset = this.item.readFrom(buf, offset);
			}
		}
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
		{
			long value =
					(((long) buf[offset++] & 0xFF)
							| (((long) buf[offset++] & 0xFF) << 8)
							| (((long) buf[offset++] & 0xFF) << 16)
							| (((long) buf[offset++] & 0xFF) << 24)
							| (((long) buf[offset++] & 0xFF) << 32)
							| (((long) buf[offset++] & 0xFF) << 40)
							| (((long) buf[offset++] & 0xFF) << 48)
							| (((long) buf[offset++] & 0xFF) << 56));
			this.value = Double.longBitsToDouble(value);
		}
		{
			if (this.arr == null) {
				this.arr = new Integer[ARR_LENGTH];
			}
			for (int i = 0; i < ARR_LENGTH; i++) {
				int el;
				el =
						(int)
								((buf[offset++] & 0xFF)
										| ((buf[offset++] & 0xFF) << 8)
										| ((buf[offset++] & 0xFF) << 16)
										| (buf[offset++] << 24));
				this.arr[i] = el;
			}
		}
		{
			int size = 0;
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
						throw new RuntimeException("Invalid list size data");
					}
				}
				size = value | (rb << i);
			}
			if (this.li == null) {
				this.li = new java.util.ArrayList<Integer>(size);
			}
			for (int i = 0; i < size; i++) {
				int el;
				el =
						(int)
								((buf[offset++] & 0xFF)
										| ((buf[offset++] & 0xFF) << 8)
										| ((buf[offset++] & 0xFF) << 16)
										| (buf[offset++] << 24));
				this.li.add(el);
			}
		}
		return offset;
	}

	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {}
}