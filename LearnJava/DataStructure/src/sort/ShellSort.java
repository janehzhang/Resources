package sort;

public class ShellSort {

	
	public static void shellSort(int[] a){			//7分钟搞掂
		
		
	}
	public static void main(String[] args) {
		
		int[] a = {2,454,3,3,342,4444,1,13,13,0,0,2,67,23};
		shellSort(a);
		for(int i = 0; i < a.length; i++){
			System.out.print(a[i]+" ");
		}
		
		
	}
	/*
	public static void shellSort(int[] a){
		int i,j,k,temp;
		int len = a.length;
		for(k = len/2; k > 0; k/=2){
			for( i = k; i < len; i+=k){
				j = i;
				temp = a[i];
				if(a[i] < a[i-k]){
					for(; j > 0 && a[j-k] > temp; j-=k){
						a[j] = a[j-k];
					}
				}
				a[j] = temp;
			}
		}
		
	}
	*/
}
