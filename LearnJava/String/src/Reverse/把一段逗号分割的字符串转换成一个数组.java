package Reverse;

public class 把一段逗号分割的字符串转换成一个数组 {

	//如何把一段逗号分割的字符串转换成一个数组?
	//用StringBuffer很方便啊
	
	
	public static void main(String[] args) {
		
		String s = "d d,d,dfaf,";
		char[] ch = s.toCharArray();
		boolean flag = false;
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < ch.length; i++){
			if(ch[i] == ','){
				flag = true;
				System.out.println(sb);
				sb.delete(0, sb.length());
			}else{
				sb.append(ch[i]);
			}
		}
		
		System.out.println(sb);
		
		
		
	}
	
	
	/*
	 *  
	 *  String s = "d,d,d,dfaf,";
		char[] array = s.toCharArray();
		boolean flag = false;
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < array.length; i++){
			if(array[i] == ','){
				flag = true;
				System.out.println(sb);
				sb.delete(0, sb.length());
			}
			if(!flag){
				sb.append(array[i]);
			}
			flag = false;
		}
	 */
}
