import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;

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
		this.li = new ArrayList<Integer>();
	}
	public void writeTo(ByteBuffer buf) throws IOException {
		this.item.writeTo(buf);
		Bits.writeString(buf, this.name);
		buf.putDouble(this.value);
		for (int i = 0; i < ARR_LENGTH; i++) {
			buf.putInt(this.arr[i]);
		}
		Bits.writeUnsignedVarInt(buf, this.li.size());
		for (int i = 0; i < this.li.size(); i++) {
			int liValue = this.li.get(i);
			buf.putInt(liValue);
		}
	}
	private void writeObject(ObjectOutputStream oos) throws IOException {
	}
	public void readFrom(ByteBuffer buf) throws IOException {
		if(item == null) {
			item = new Item();
		}
		item.readFrom(buf);
		this.name = Bits.readString(buf);
		this.value = buf.getDouble();
		if(this.arr == null) {
			this.arr = new Integer[ARR_LENGTH];
		}
		for (int i = 0; i < ARR_LENGTH; i++) {
			this.arr[i] = buf.getInt();
		}
		int liSize = Bits.readUnsignedVarInt(buf);
		if(this.li == null) {
			this.li = new ArrayList<Integer>();
		}
		for (int i = 0; i < liSize; i++) {
			this.li.add(buf.getInt());
		}
	}
	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
	}
}