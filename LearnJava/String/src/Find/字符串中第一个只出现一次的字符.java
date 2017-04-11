package Find;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

//字符串中第一个只出现一次的字符


public class 字符串中第一个只出现一次的字符{

	//你用hashMap来保存不行啊，hashmap是无序的
	/*
	public static Object get(String s){
		Map<Object,Integer> map = new TreeMap<Object, Integer>();
		char[] ch = s.toCharArray();
		for(int i = 0; i < ch.length; i++){
			if(map.containsKey(ch[i])){
				map.put(ch[i], map.get(ch[i]) +1);
			}else{
				map.put(ch[i], 1);
			}
		}
		Iterator i = map.entrySet().iterator();
		while(i.hasNext()){
			Map.Entry entry = (Entry) i.next();
			Integer value = (Integer) entry.getValue();
			if( value == 1){
				return entry.getKey();
			}
		}
		return null;
		
	}
	*/
	
	//常用方法,可以放所有字符的数组
	public static Object get(String s){
		char[] ch = s.toCharArray();
		int[] temp = new int[255];
		for(int i = 0; i < ch.length; i++){
			temp[ch[i]] += 1;
		}
		for(int i = 0; i < ch.length; i++){
			if(temp[ch[i]] == 1){
				return ch[i];
			}
		}
		return null;
	}
	
	
	public static void main(String[] args) {
		System.out.println(get("hello"));
	}

	
}
