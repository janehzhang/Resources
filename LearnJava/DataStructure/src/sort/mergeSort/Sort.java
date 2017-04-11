package sort.mergeSort;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class Sort {
	//�鲢����
	static public void mergeSort(List<Comparable> a,int p,int r){
		
		if(p<r){
			int q = (p+r)/2;
			mergeSort(a,p,q);
			mergeSort(a,q+1,r);
			Merge.merge(a, p, q, r);
			
		}
	}
	
	public static void main(String[] args) {
		Integer a[] = {1,23,21,2,5,12,32,55,33,20};
		ArrayList<Integer> A = new ArrayList<Integer>();
		 for(int i=0;i<10;i++){
			 A.add(a[i]);
		 }
		 Sort.mergeSort((List)A, 0, 9);
		 System.out.println(A);
	}

}
