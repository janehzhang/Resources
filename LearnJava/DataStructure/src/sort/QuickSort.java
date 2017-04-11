package sort;

public class QuickSort {

	
	
	private static void quickSort(int[] a, int low, int high) {
		
		
		
		
		
	}


	public static void main(String[] args) {

		int[] a = {2,454,3,3,342,4444,1,13,13,0,0,2,67,23};
		quickSort(a, 0, a.length - 1);
		for(int i = 0; i < a.length; i++){
			System.out.print(a[i]+" ");
		}
		
	}
	
	/*
	 * private static void quickSort(int[] a, int low, int high) {
		
		int i,j;
		if(low >= high){
			return;
		}
		i = low; j = high;
		int temp = a[i];
		while(i < j){
			while(i < j && a[j] > temp) j--;
			if( i < j) a[i++] = a[j];
			while(i < j && a[i] < temp) i++;
			if( i < j) a[j--] = a[i];
		}
		a[i] = temp;
		quickSort(a, i + 1, high);
		quickSort(a, low, i - 1);
	}
	 */
}
