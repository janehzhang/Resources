package Reverse;

public class 递归字符串反转{

	
	public static void main(String[] args){
		
		String s = "123456";
		String ss = reverseString(s);
		System.out.println(ss);
	}

	private static String reverseString(String s) {
		
		if(s.length()<2||s==null)return s;
		return reverseString(s.substring(1))+s.charAt(0);
		
	}
}
