package sort;

public class TestMergeSort {
 
	public static void merge(int[] a,int p,int q,int r){
		int i,j,k;
		int n1 = q-p+1;
		int n2 = r-q;
		int L[] = new int[n1];
		int R[] = new int[n2];
		for(i=0,k=p;i<n1;i++,k++){
			L[i] = a[k];
		}
		for(i=0,k=q+1;i<n2;i++,k++){
			R[i] = a[k];
		}
		for(k=p,i=0,j=0;i<n1&&j<n2;k++){
			if(L[i]<R[j]){					//升序
				a[k]=L[i];
				i++;
			}else{
				a[k]=R[j];
				j++;
			}
		}
		if(i<n1){
			for(j=i;j<n1;k++,j++){
				a[k]=L[j];
			}
		}
		if(j<n2){
			for(i=j;i<n2;k++,i++){
				a[k]=R[i];
			}
		}
	}
	
	public static void mergeSort(int[]a,int p,int r){
		if(p<r){
			int q=(p+r)/2;
			mergeSort(a,p,q);
			mergeSort(a,q+1,r);
			merge(a,p,q,r);
		}
		
		for(int k=0;k<a.length;k++){
			System.out.print(a[k]+" ");
		}
		System.out.println("\n");
		
	}
	
	public static void main(String[] args){
	
		int a[] = {4,1,3,4,87,32,2,43,15,6};
		mergeSort(a,0,a.length-1);
		
	}
}
