package DeleteRepeat;


//Bit-map，删除重复值！！！！！！大数据处理！！！！

public class DeleteRepeat{
	
	
	public static void delete1(String str){
		
		char[] ch = str.toCharArray();
		char[] result = new char[ch.length];	
		int count = 0;		
		
		int[] temp =new int[256];
		for(int i=0; i<temp.length; i++){
			temp[i] = 0;
		}
		
		for(int i = 0; i < ch.length; i++){
			if(temp[ch[i]] == 0){
				result[count++] = ch[i];
				temp[ch[i]]+=1;
			}
		}
		
		String s = new String(result);
		s = s.substring(0,count);
		System.out.print(s+"\n");
	}

	public static void delete2(String str){
		
		char[] ch = str.toCharArray();
		char[] result = new char[ch.length];   
		
		
		
		
	}
	
	/*
	public static void delete2(String str){
		
		char[] ch = str.toCharArray();
		char[] result = new char[ch.length];   
		int count = 0;
		
		int[] temp =new int[8];
		for(int i=0; i<temp.length; i++){
			temp[i] = 0;
		}
		
		for(int i = 0; i < ch.length; i++){
			
			int index = ch[i]/32;
			int shift = ch[i]%32;
			
			if( (temp[index] & (1 << shift)) == 0){
				result[count++] = ch[i];
				temp[index] |= (1 << shift);
			}
		}
		
		String s = new String(result);
		s = s.substring(0,count);
		System.out.print(s);
		
	}
	*/
	public static void main(String[] args) {
		delete1("hello llolle");
		
		delete2("hello llolle");
	}
}
