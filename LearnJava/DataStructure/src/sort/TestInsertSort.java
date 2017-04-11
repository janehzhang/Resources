package sort;

public class TestInsertSort {

	public static void insertSort(int[] a){
		if(a!=null){
			for(int i=1;i<a.length;i++){
				int j=i;
				int temp = a[i];
				if(a[j-1]>temp){					//升序
					while(j<=1&&a[j-1]>temp){
						a[j]=a[j-1];
						j--;
					}
				}	
				a[j]=temp;
			}
		}
		
		for(int k=0;k<a.length;k++){
			System.out.print(a[k]+" ");
		}
		System.out.println("\n");
		
	}
	
	public static void main(String[] args){
		int a[] = {4,1,3,4,87,32,2,43,15,6};
		insertSort(a);
		
	}
	
}
