package sort.mergeSort;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class Merge {
	 //�����������еĺϲ���������
	 public static void merge(List<Comparable> a,int p, int q, int r){
	        int i,j,k,
	        n1=q-p+1, //[p..q]�ĳ���
	        n2=r-q; //[q+1..r]�ĳ���
	        Comparable[] L=new Comparable[n1],
	                     R=new Comparable[n2]; 
	        for(i=0;i<n1;i++)//��a[p..q]������L
	            L[i]=a.get(p+i);
	        for(j=0;j<n2;j++)//��a[q+1..r]������R
	            R[j]=a.get(q+1+j);
	        i=j=0;
	        k=p;
	        while(i<n1&&j<n2)
	            if(L[i].compareTo(R[j])<0)  		//����
	                a.set(k++, L[i++]);
	            else
	                a.set(k++, R[j++]);
	        if(i<n1)//��L[i..n1]������a[k..r]
	            for(;i<n1;i++)
	                a.set(k++, L[i]);
	        if(j<n2)//��R[j..n2]������a[k..r]
	            for(;j<n2;j++)
	                a.set(k++, R[j]);
	    }
	 
	 public static void main(String[] args){
		 
		 Integer a[] = {1,4,21,33,54,12,32,59,60,80};
		 ArrayList<Integer> A = new ArrayList<Integer>();
		 for(int i=0;i<10;i++){
			 A.add(a[i]);
		 }
		 Merge.merge((List)A, 0, 4, 9);
		 System.out.println(A);
		 
		 
	 }
	 
}
