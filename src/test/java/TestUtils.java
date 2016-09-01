public class TestUtils {

	public static void printArray(byte[] arr ){
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i]);
			if(i < arr.length - 1){
				System.out.print(", ");
			}
		}
		System.out.println();
	}

	public static <T>void printArray(T[] arr ){
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i]);
			if(i < arr.length - 1){
				System.out.print(", ");
			}
		}
		System.out.println();
	}

}
