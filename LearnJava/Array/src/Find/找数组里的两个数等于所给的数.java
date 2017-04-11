package Find;

import java.util.Arrays;

/*
 * 笔试题
	给定一个有序!!!数组a，长度为len，和一个数X，判断A数组里面是否存在两个数，他们的和为X。
	bool judge(int *a, int len, int x)
*/

public class 找数组里的两个数等于所给的数 {

	//遍历，时间复杂度为O(N2)，很显然，这种方法没什么实际意义
	static boolean judge(int[] a, int len, int x){
		
		for(int i = 0; i < len-1; i++){
			int other = x - a[i];
			for(int j = i+1; j < len; j++){
				if(other == a[j]){	
					return true;
				}
			}
		}
		return false;
	}
	
/*
   * 申请一个与原数组a[N]一样长度的内存空间b[N]，用给定的值X减去原数组中的元素，对应的放到申请的内存空间arr[N]中，
  　　　设置两个指针p和q，分别指向原数组a[N]的最后一个元素和b[N]的第一个元素，指针移动满足条件：
	   每次只移动一个指针！若 值较大的指针；若原数组是降序的，那么每次移动指向的值较小的指针；
	   若指针指向的值相等  且i不等于j（添加了条件：i不等于j，避免出现8 = 4 + 4的情况），则返回true；
	  
	  移动结束，跳出移动指针的循环，说明不存在，返回false
	 */
	static boolean judge2(int[] a, int len, int x){
		
		int[] b = new int[len];
		for(int i = 0; i < len; i++){
			b[i] = x - a[i];
		}
		int a_index = len - 1;
		int b_index = 0;
		
		//判断a是升序还是降序
		int is_rise = 0;
		is_rise = a[1] > a[0] ? 1 : 0; 
		
		while(a_index >= 0 && b_index < len){
			if( a[a_index] > b[b_index]){
				switch(is_rise){
					case 1: a_index --; break;
					case 0: b_index ++; break;
					default: break;
				}
			}
			else if( a[a_index] == b[b_index] && a_index != b_index){
				return true;
			}
			else{
				switch(is_rise){
				case 1: b_index ++; break;
				case 0: a_index --; break;
				default: break;
			}
			}
		}
		
		return false;
		
	}
	
	
	//面试宝典267页，如果没有排序，就先排序，再分别从后往前，从前往后遍历
	public static boolean judge3(int[] a, int len, int x){
		int result = 0;
		Arrays.sort(a);
		int begin = 0;
		int end = len - 1;
		while(begin < end && end <= len-1){
			if(a[begin] + a[end] == x){
				System.out.println(a[begin] + " " + a[end]);
				begin ++;
				end --;
				result ++;
			}
			else if(a[begin] + a[end] < x){
				begin ++;
			}else{
				end ++;
			}
		}
		
		
		if(result != 0){
			return true;
		}
		return false;
	}
	
	
	public static void main(String[] args) {
		int[] a = {1,2,3,4,5,6,6,6};
		System.out.print(judge3(a,8,10));
	}

}
