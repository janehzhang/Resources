package sort;

public class TestSelectSort {
	
	public static void selectSort(int[] a){
		int i,j;
		int temp=0;
		int flag=0;
		for(i=0;i<a.length;i++){
			temp=a[i];
			flag=i;
			for(j=i+1;j<a.length;j++){
				if(a[j]<temp){				//升序
					temp=a[j];
					flag=j;
				}
			}
			if(flag!=i){
				a[flag]=a[i];
				a[i]=temp;
			}
		}
		for(int k=0;k<a.length;k++){
			System.out.print(a[k]+" ");
		}
		System.out.println("\n");
	}
	
	public static void main(String[] args){
		int a[] = {4,1,3,4,87,32,2,43,15,6};
		selectSort(a);
	}
}
