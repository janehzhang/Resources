package Move;

import java.util.Arrays;

//这不就和那个让我心痛的reverseString一样嘛？

public class 数组循环右移k位 {

	public static void move(int[] a, int k){
		
		int len = a.length;
		reverse(a, 0, len - k - 1);
		reverse(a, len - k ,len - 1);
		reverse(a,0,len - 1);
		
	}
	
	private static void reverse(int[] a, int i, int j) {
		
	}

	public static void main(String[] args) {
		int[] a = {1,2,2,2,2,4,3};
		move(a,3);
	}

}
