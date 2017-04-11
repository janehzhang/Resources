package EliminateBracket;

//去除括号

public class RemoveBracket {

	public static void remove2(String s){
		char[] ch = s.toCharArray();
		int bracket_num = 0; 
		String result = "(";
		
		for(int i = 0; i < ch.length; i++){
			if(ch[i] == '('){
				bracket_num ++;
			}
			else if(ch[i] == ')'){
					if(bracket_num > 0){
						bracket_num --;
					}else{
						System.out.print("illeagal");
						return;
					}
			}
			else if(ch[i] >= '0' && ch[i] <= '9' || ch[i] == ','){
				   result += ch[i];
			}
			else{
				System.out.print("illeagal");
				return;
			}
		}
		if(bracket_num != 0){
			System.out.print("illeagal");
			return;
		}
		result += ')';
		System.out.println(result);
		return;
	}
	
	
	//我写的，这样就把原字符串改变了啊
	public static void remove(String s){
		char[] ch = s.toCharArray();
		int bracket_num = 0; 
		
		for(int i = 0; i < ch.length; i++){
			
			if(ch[i] == '(' || ch[i] == ',' || ch[i] == ')' || ch[i] >= '0' || ch[i] <= '9'){
				if(ch[i] == '('){
					ch[i] = '\0';
					bracket_num +=1;
				}
				if(ch[i] == ')' && bracket_num > 0){
					ch[i] = '\0';
					bracket_num --;
				}
			}
			else{
				System.out.println("illeagel");
				return;
			}
		}
		if(bracket_num != 0){
			System.out.println("illeagel");
			return;
		}
		for(int i = 0; i < ch.length; i++){
			if(ch[i]!= '\0'){
				System.out.print(ch[i]);
			}
		}
	
	}

	public static void main(String[] args) {
		String s = "(1,(2,3),(4,(5,6),7))";
		remove2(s);
	
	}

}
