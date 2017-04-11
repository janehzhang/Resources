package Reverse;
import java.util.Arrays;


//反转句子（永远的痛，要出发笔试）

/*
 char[] data = str.toCharArray(); 
 String str = new String( char data[]); 

 char charAt(int index); 
 int indexOf(int ch);     
 byte[] getBytes()  	 
 
 
 StringBuffer:
 StringBuffer delete(int start,int end)  //Removes the characters in a substring of this sequence. 
 StringBuffer insert(int offset,char[] str)
 StringBuffer reverse()   
             
 StringBuffer用append(),String用 + 来连接
      
                           
 Arrays.binarySearch(a[], key); 
 Arrays.sort(a[]);				
 Arrays.toString(a[]);			
*/


/*
 * Integer.toString(num);     //int ->  String
 * Integer.parseInt(str);     //String -> int 
 */


public class ReverseString {
	
	public static String reverse(String s){
		if(s == null){
			return null;
		}
		char[] c = s.toCharArray();
		toReverse(c,0,c.length-1);
		
		int start = 0;
		for(int i=0; i<c.length; i++){
			if(c[i]==' '){
				toReverse(c,start,i-1);
				start = i+1;
			}
		}
		toReverse(c,start,c.length-1);
		return new String(c);
	}
	
	private static void toReverse(char[] c, int i, int length) {
		char temp;
		while(i<length){
			temp = c[i];
			c[i] = c[length];
			c[length] = temp;
			i++;
			length--;
		}
	}

	//StringBuffer可以reverse()
	public static void reverse2(String s){     
		String[] str = s.split(" ");
		for(int i =0,j = str.length-1; i < j; i++,j--){
			String temp = str[i];
			str[i] = str[j];
			str[j] = temp;
		}
		for(int i = 0; i < str.length; i++){
			System.out.print(str[i]+" ");
		}
	}
	
	
	public static void main(String[] args) {
		String s = reverse("you are beautiful.");
		System.out.print(s+"\n");
		
		reverse2("you are beautiful.");
		
		
	}
}
