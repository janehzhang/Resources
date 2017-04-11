package IfTwoStringIsEqual;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


//�ж������ַ����Ƿ�����ͬ��ĸ��ɣ��Ҹ���ĸ����Ҳ��ȡ�

public class IsEqual {
	
	//����һ
	private static boolean compare(String s1, String s2) {
		
		byte[] byte1 = s1.getBytes();
		byte[] byte2 = s2.getBytes();
		
		Arrays.sort(byte1);
		Arrays.sort(byte2);
		
		s1 = new String(byte1);			//����
		s2 = new String(byte2);
		
		if(s1.equals(s2)){
			return true;
		}else{
			return false;
		}
	}
	
	//���������ռ任ʱ��
	public static boolean compare2(String s1, String s2){
		
		int map[] = new int[256];	//��ʼ�����Ǹ���Ԫ����0��
		
		byte[] byte1 = s1.getBytes();
		byte[] byte2 = s2.getBytes();
		for(int i = 0; i < byte1.length; i++){
				map[byte1[i] - '0'] ++;
		}
			
		/*����������񲻺�
		   if(byte1[i] != ' '){
			 map[byte1[i]] ++;
		   }
		*/
		
		for(int i = 0; i < byte2.length; i++){
			map[byte2[i] - '0'] --;
		
		}
		for(int i = 0; i < map.length; i++){
			if(map[i]!=0){
				return false;
			}
		}
		return true;
		
	}
	
	
	//��mapд������
	public static boolean isEqual(String s1, String s2){
		if(s1 == null || s2 == null){
			return false;
		}
		char[] c1 = s1.toCharArray();
		char[] c2 = s2.toCharArray();
		Map<Object,Integer> map1 = new HashMap<Object,Integer>();
		for(int i = 0; i < c1.length; i++){
			if(map1.containsKey(c1[i])){		//��ô����map��value
				map1.put(c1[i], map1.get(c1[i])+1);
			}else if(c1[i] != ' '){
				map1.put(c1[i], 1);
			}
		}
		
		for(int i = 0; i < c2.length; i++){
			if(map1.containsKey(c2[i])){	
				map1.put(c2[i], map1.get(c1[i])-1);
			}else if(c1[i] != ' '){
				return false;
			}
		}
	
		
		return false;
	}
	
	public static void main(String[] args) {
		String s1 = "abcd";
		String s2 = "abcd";
		System.out.println(compare(s1,s2));
		System.out.println(compare2(s1,s2));
		
	}
	
}
