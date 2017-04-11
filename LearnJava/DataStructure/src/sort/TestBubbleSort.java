package sort;

public class TestBubbleSort {

	public static void bubbleSort(int[] a){
		int i,j,temp;
		int len = a.length;
		for(i=0;i<len-1;++i){
			for(j=len-1;j>i;--j){
				if(a[j-1]>a[j]){
					temp = a[j];
					a[j] = a[j-1];
					a[j-1] = temp;
				}
			}
		}
		for(int k=0;k<a.length;k++){
			System.out.print(a[k]+" ");
		}
		System.out.println("\n");
	}
	
	public static void main(String[] args){
		int a[] = {4,1,3,4,87,32,2,43,15,6};
		bubbleSort(a);
	}
}
