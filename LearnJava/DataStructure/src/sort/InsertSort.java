package sort;

public class InsertSort {
	
	public static void insertSort(int[] a){		 //3分钟搞定
		
		
		
	}
	
	
	
	public static void main(String[] args) {
		int[] a = {2,454,3,3,342,4444,1,13,13,0,67,23};
		insertSort(a);
		for(int i = 0; i < a.length; i++){
			System.out.print(a[i]+" ");
		}
	}
	
	
	
	/*
	public static void insertSort(int[] a){
		int i,j,temp;
		int len = a.length;
		for(i = 1; i < len; i++){
			j = i;
			temp = a[i];
			if(a[i] < a[i-1]){
				for(; j > 0 && a[j - 1] > temp; j--){
					a[j] = a[j - 1];
				}
			}
			a[j] = temp;
		}
	}
	*/
}
