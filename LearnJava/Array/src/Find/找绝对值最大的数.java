package Find;

//会了,用二分法！！！


public class 找绝对值最大的数 {

	//二分查找 找正负数的交界
	public static int find(int[] a,int low, int high){
		
		
		if(low >= high){
			return 0;
		}
		int mid = (low + high)/2;
		if(a[mid] == 0){
			return mid;
		}
		else if(a[mid] > 0){
			if(a[mid - 1] <= 0){
				return mid - 1;
			}
			else {
				high = mid;
				return find(a, low, high);
			}
		}
		else if(a[mid] < 0){
			if(a[mid + 1] >= 0){
				return mid;
			}else if(a[mid + 1] < 0){
				low = mid;
				return find(a, low, high);
			}
		}
		return 0;
		
	}
	
	public static void main(String[] args) {
		int[] a = {-10, -5, -2, -1, 2, 3, 5};
		
		System.out.print(
				a[ find(a,0,6) ]
		);
		
	}

}
