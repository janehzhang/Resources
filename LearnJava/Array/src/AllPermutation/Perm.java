package AllPermutation;


//全排列就是从第一个数字起每个数分别与它后面的数字交换!!!!!!!!!!!!!!!!!!!!!!!!!!


public class Perm{
	
	private static void perm(char[] ch, int from, int to) {
		if(from == to){
			for(int i = 0; i <= to; i++){
				System.out.print(ch[i]);
			}
			System.out.println("\n");
			return;
			
		}
		for(int i = from; i <= to; i++){
			char temp = ch[i];
			ch[i] = ch[from];
			ch[from] = temp;
			perm(ch, from + 1, to);
			temp = ch[i];
			ch[i] = ch[from];
			ch[from] = temp;
		}
	}
	
	//去除重复字符的递归算法
	//带重复字符的全排列就是从第一个字符起每个数分别与它后面非重复出现的数字交换。
    //即：第i个数与第j个数交换时，要求[i,j)中没有与第j个数相等的数。
	public static void perm2(char[] ch, int from, int to){
		if(from == to){
			for(int i = 0; i <= to; i++){
				System.out.print(ch[i]);
			}
			System.out.println("\n");
			return;		
		}
		for(int i = from; i <= to; i++){
			if(!isSwap(ch, from, i)){		//这里做判断
				continue;
			}
			char temp = ch[i];
			ch[i] = ch[from];
			ch[from] = temp;
			perm(ch, from + 1, to);
			temp = ch[i];
			ch[i] = ch[from];
			ch[from] = temp;
		}
		
	}

	
	
	private static boolean isSwap(char[] ch, int from, int to) {
		boolean flag = true;
		for(int j = from ; j < to; j++){
			if(ch[j] == ch[to]){
				flag = false;
				break;
			}
		}
		return flag;
	}

	
	public static void main(String[] args) {
		String s = "122";
		char[] ch = s.toCharArray();
		perm(ch ,0 ,ch.length-1);
	}



	
	
	
	
	
	/*
	public void perm(int[] a, int begin, int length){
		
		if(begin == length){
			System.out.println(a[begin]);
		}
		for(int i = begin; i <= length; i++){
			swap(a[begin], a[i]);
			perm(a, begin+1, length);
			swap(a[i],a[begin]);
		}
		
	}
	*/
}
