package sort;

public class SelectSort {

	public static void selectSort(int[] a){   //3分钟搞定
		
		
		
	}
	
	
	
	public static void main(String[] args) {
		int[] a = {2,454,3,3,342,4444,1,13,13,0,67,23};
		selectSort(a);
		for(int i = 0; i < a.length; i++){
			System.out.print(a[i]+" ");
		}
	}
	
	/*
	public static void selectSort(int[] a){
		int i,j,index,temp;
		int len = a.length;
		for(i = 0; i < len - 1; i++){
			temp = a[i];
			index = i;
			for(j = i + 1; j < len; j++){
				if(a[j] < temp){
					index = j;
					temp = a[j];
				}
			}
			if(i != index){
				a[index] = a[i];
				a[i] = temp;
			}
		}
	}
    */
}
