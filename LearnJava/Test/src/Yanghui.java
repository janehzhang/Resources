public class Yanghui{
	
	public static void print(int n){
		
		int[][] arr = new int[n][n];
		System.out.println(1);
		
		for(int i = 0; i < n; i++){
			int row = i;
			int cloumn = 0;
			arr[row][cloumn] = 1;
			System.out.print(arr[row][cloumn++] + " ");
			
			int count = i;
			while(count > 0){
				arr[row][cloumn] = arr[row-1][cloumn-1] + arr[row-1][cloumn];
				System.out.print(arr[row][cloumn] + " ");
				cloumn ++;
			}
			
			arr[row][cloumn] = 1;
			System.out.println(arr[row][cloumn]);
		}
		
	}
	public static void main(String[] args) {
		
		print(5);
		
	}
}