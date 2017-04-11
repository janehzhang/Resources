package Find;

//怕以后又忘了

//剪枝法

public class 找数组中第k小的数 {

	public static void find(int[] a, int k){
		
		int index = quickSort(a,0,a.length-1,k);
		System.out.println(a[index]);
	}

	public static int quickSort(int[] a, int low, int high, int k){
		
		
		
		/*
		if(low > high){
			return Integer.MIN_VALUE;
		}
		int i = low;
		int j = high;
		int temp = a[i];
		while(i < j){
			while(a[j] >= temp && i < j) j--;
			a[i++] = a[j];
			while(a[j] <= temp && i < j) i++;
			a[j--] = a[i];
		}
		a[i] = temp;
		if(i == k-1){
			return temp;
		}else if(i < k-1){
			return quickSort(a, i, high, k);
		}else{
			return quickSort(a, low, j, k );
		}
		*/
		return 0;
	}
	

	public static void main(String[] args){
		int[] a = {1,5,2,6,8,0,6};
		find(a,4);
	}
}
