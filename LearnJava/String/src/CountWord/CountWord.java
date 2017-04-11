package CountWord;

public class CountWord {

	
	public static void count(String s){
		
		int count = 0;
		int word = 0;
		char[] c = s.toCharArray();
		for(int i = 0; i < c.length; i++){
			
			if(c[i] == ' '){
				word = 0;
			}else
				if(word == 0){
					word = 1;
					count++;
				}
			
		}
		System.out.print(count+" words");
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		count("you are so beautiful");
	}

}
