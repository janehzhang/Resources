package 动态规划;

//求职宝典262页,不会

public class 找最大子数组之和 {
  
	//方法一：记录下上一个
	public void getMaxSub(int[] a){
	
		
	}
	
	
	//方法二：动态规划 ：子数组和All[i-1] = max ( All[i-2],End[i-1],a[i-1] )
//	public static void getMaxSub2(int[] a){
//	
//		int size = a.length;
//		int[] All = new int[size];
//		int[] End = new int[size];
//		
//		All[0] =a[0];
//		End[0] = a[0];
//		All[size-1] =a[size-1];			//这两句不知道什么用
//		End[size-1] = a[size-1];
//		
//		for(int i = 1; i < size; i ++){
//			
//			
//			End[i] = max(a[i],End[i-1]+a[i]);
//			All[i] = max(End[i], All[i-1]);
//		}
//		
//		System.out.println("maxSubArray_Sum:" + All[size-1]);
//		
//	}
	
	//方法三：优化动态规划，不用数组来保存了
    public static void getMaxSub3(int[] a){
		
	}
	
    //方法四：还有求位置
    public static void getMaxSub4(int[] a){
    	
    }
    
    
	private static int max(int x, int y) {
		return x>y?x:y;
	}

	
	
	
	public static void main(String[] args) {
		int[] a = {1,-2,4,8,-4,10,-1,-5};
		getMaxSub3(a);
		
	}

}
