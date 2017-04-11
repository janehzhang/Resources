package Find;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class 找出两个字符串数组中的相同元素 {

	public static void find(String[] strArr1,String[] strArr2){
		
		if(strArr1 == null || strArr2 == null) {  
            return;  
        }
		
		Arrays.sort(strArr1);
		Arrays.sort(strArr2);
		
		List<String> list = new ArrayList();
		int i = 0,k = 0;
		
		while(i < strArr1.length && k < strArr2.length){
			if(strArr1[i].compareTo(strArr2[k]) == 0){
				if(strArr1[i].equals(strArr2[k])){
					list.add(strArr1[i]);
					i++;
					k++;
				}
			}
			else  if(strArr1[i].compareTo(strArr2[k])<0){  
	           i++;  
	        } else {  
	           k++;  
	        }  
		}
		
		System.out.println(list);
	}
	
	public static void main(String[] args) {
		
		String[] strArr1 = {"xiaoxin","niutou","shanqiu","luobo" };  
        String[] strArr2 = {"xiaoxin","ggg","shanqiu","meile","dddsf","niutou"};  
		find(strArr1, strArr2);
	}

}
