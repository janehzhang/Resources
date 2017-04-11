package Find;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//会


public class 找出现次数最多的元素 {

	public static int find(int[] a){
		
		int result = 0;
		Map<Integer,Integer> map = new HashMap<Integer,Integer>();
		for(int i = 0; i < a.length; i++){
			if(map.containsKey(a[i])){
				map.put(a[i], map.get(a[i])+1);
			}else{
				map.put(a[i], 1);
			}
		}
		int most = 0;
		Iterator i = map.entrySet().iterator();
		while(i.hasNext()){
			Map.Entry entry = (Map.Entry)i.next();
			int count = (Integer) entry.getValue();
			int key = (Integer)entry.getKey();
			if(count>most){
				most = count;
				result = key;
			}
		}
		return result;
		
	}
	
	public static void main(String[] args) {
		int[] a = {1,2,2,2,2,4,3};
		System.out.print(find(a));
	}

}
