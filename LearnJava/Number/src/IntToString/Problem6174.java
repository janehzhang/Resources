package IntToString;

import java.util.Arrays;

//非独立


/*入门经典77页：给一个四位整数num，从大到小排序得到a，从小到大排序得到b，  然后a-b作为num ，然后继续。
        比如 8532-2358=6174，然后 7641-1467=7641，回到了自己
*/

/*
 * Integer.toString(num);     //int ->  String
 * Integer.parseInt(str);     //String -> int 
 */

public class Problem6174 {

	public static int getNext(int num){
		String s = Integer.toString(num);
		char[] c = s.toCharArray();
		Arrays.sort(c);
		String max = new String(c);
		int max_num = Integer.parseInt(max);
		
		int i = 0;
		int length =c.length-1;
		while(i<length){
			char temp = c[i];
			c[i] = c[length];
			c[length] = temp;
			i++;
			length--;
		}
		String min = new String(c);
		int min_num = Integer.parseInt(min);
		return Math.abs(max_num-min_num);
		
	}
	
	
	private static void circle(int number) {
		
		/*
		int[] num = new int[100];
		int count = 0;
		num[count] = number;
		boolean flag = false;
		while(true){
			System.out.print(num[count]+"->");
			
			//生成下一个
			num[count+1] = getNext(num[count]);
			
			//判断是否在num里已经有了
			for(int i = 0; i < count; i++){
				if(num[i]==num[count+1]){
					flag = true; break;
				}
			}
			
			//如果找到，退出循环
			if(flag){
				break;
			}
			count++;
		}
		*/
		
	}
	
	public static void main(String[] args){
		circle(1234);
	}

	
}
