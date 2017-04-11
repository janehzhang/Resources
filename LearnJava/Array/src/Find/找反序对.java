package Find;


//想个好方法 。第288页
//再来一次

public class 找反序对 {

	
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/*
	 * private static int reverseCount = 0;
	public static void mergeSort(int[] a, int low, int high){
		if(low < high){
			int mid = (low + high)/2;
			mergeSort(a, low, mid);          
			mergeSort(a, mid + 1, high);
			merge(a, low, mid, high);
		}
		
	}
	private static void merge(int[] a, int low, int mid, int high) {
		
		int i, j, k = 0;
		int n1 = mid - low + 1;
		int n2 = high - mid;
		
		int[] L = new int[n1];
		int[] R = new int[n2];
		
		for(i = 0, k = low; i < n1; k++, i++){
			L[i] = a[k];
		}
		for(i = 0, k = mid + 1; i < n2; k++, i++){
			R[i] = a[k];
		}
		
		for(k = low, i = 0, j = 0; i < n1 && j < n2; k++){
			if(L[i] < R[j]){
				a[k] = L[i++];
			}else{
				reverseCount += mid - i + 1;		//？？？？？
				a[k] = R[j++];
			}
		}
		if(i < n1){
			for(; i < n1; i++,k++){
				a[k] = L[i];
			}
		}
		if(j < n2){
			for(; j < n2; j++,k++){
				a[k] = R[j];
			}
		}
		
		
	}
	 */
}
