package sort;

public class BubbleSort {
	
	public static void bubbleSort2(int[] a){	 //3分钟搞定
		
	}
	
	public static void bubbleSort(int[] a){
		
		int i,j,temp;
		int len = a.length;
		for(i = 0; i < len - 1; i++){
			for(j = len - 1; j > 0; j--){
				if(a[j] < a[j - 1]){
					temp = a[j];
					a[j] = a[j-1];
					a[j-1] = temp;
				}
			}
		}
		
	}
	public static void main(String[] args) {
		int[] a = {2,454,3,3,342,4444,1,13,13,0,67,23};
		bubbleSort2(a);
		for(int i = 0; i < a.length; i++){
			System.out.print(a[i]+" ");
		}
	}
}
