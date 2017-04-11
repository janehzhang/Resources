

public class 不用比较得到最大值 {

	
	public static void main(String[] args) {
		
		int a = 10;
		int b = 20;
		long max = get_max(a, b);
		int min = get_min(a, b);
		System.out.println(max);
		System.out.println(min);
	}

	private static int get_max(int a, int b) {
		return (a + b + Math.abs(a-b))/2;
	}
	
	private static int get_min(int a, int b) {
		return (a + b - Math.abs(a-b))/2;
	}

}
