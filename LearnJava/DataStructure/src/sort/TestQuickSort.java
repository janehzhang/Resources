package sort;

public class TestQuickSort {

	public static void quickSort(int[] a,int low,int high){
		if(low<high){
			int i = low;
			int j = high;
			int index = a[i];
			while(i<j){
				while(i<j&&a[j]>=index)
					j--;
				if(i<j){
					a[i++]=a[j];
				}
				while(i<j&&a[i]<index)
					i++;
				if(i<j){
					a[j--]=a[i];
				}
			}
			a[i]=index;
			quickSort(a,low,i-1);
			quickSort(a,i+1,high);
			
		}
	
	}
	
	
	public static void main(String[] args){
		
		int i=0;
		int a[] = {4,1,3,4,87,32,2,43,15,6};
		quickSort(a,0,a.length-1);
		
		for(int k=0;k<a.length;k++){
			System.out.print(a[k]+" ");
		}
		System.out.println("\n");
		
	}
	
}
