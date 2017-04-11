//那样想？想不到啊！！！！
/*  
 	getLessK(ElementType a[], int n, int k)实现

*/
#include <stdio.h>
#define ElementType int
#define LENGTH 20

int Partition(ElementType a[], int low, int high)
{
	int pivot = a[low];
	while(low < high){
		if(low < high && a[high] >= pivot) high--;
		a[low] = a[high];
		if(low < high && a[low] <= pivot) low++;
		a[high] = a[low];
	}
	a[low] = pivot;
	return low;
}

void getLessK(ElementType a[], int n, int k)
{
	if(a == NULL || k > n || n <= 0 || k <- 0){
		return;
	}
	int start = 0;
	int end = n-1;
	int pivotPos = Partition(a, start, end);
	while(pivotPos != k-1){
		if(pivotPos > k-1){
			end = pivotPos - 1;
			pivotPos = Partition(a, start, end);
		}else{
			start = pivotPos + 1;
			pivotPos = Partition(a, start, end);
		}
	}
	for(int i = 0; i < k; i++){
		printf("%d\n", a[i]);
	}
}


void QuickSort(ElementType a[], int low,int high)
{
	if(low < high){
		int pivotPos = Partition(a, low, high);
		QuickSort(a, low, pivotPos-1);
		QuickSort(a, pivotPos+1, high);
	}
}

int main()
{
	ElementType a[LENGTH] = {
	    12,2,43,23,33,2,1,65,8,5,
	    423,39,143543,11,0,32,76,777,76,12
	};
	QuickSort(a,0,LENGTH);
	for(int i=0;i<LENGTH;i++){
		printf("%d ", a[i]);
	}

	int k = 8;
	printf("\n\nthe less %d number in the array:\n", k);
	getLessK(a,LENGTH,k);
	getchar();
	return 0;

}