package Find;

public class 找数组中第k大的数 {

	public static int quickSort(int[] a, int low, int high, int k){			//找第k大的数

		int temp = a[low];
		while(low < high){
			while(a[high] >= temp && low < high ) high--;
			a[low++] = a[high];
			while(a[low] < temp && low < high ) low++;
			a[high--] = a[low];
		}
		a[low] = temp;
		k = a.length - k;
		if(k == low){
			return a[low];
		}else if(k > low){
			return quickSort(a,low + 1,high,k);
		}else{
			return quickSort(a,0,low - 1,k);
		}
	}
	public static void main(String[] args) {
		int[] a = {1,2,3,4,5,6,7,8,9,10};
		
		int result = quickSort(a,0,9,2);
		
		System.out.println(result);
	}

}
