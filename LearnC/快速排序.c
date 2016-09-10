/*
	交换排序：冒泡排序，快速排序。
	
	根据序列中两个元素的比较结果来对换这两个记录在序列中的位置。

	快速排序：对冒泡的一种改进。基本思想是基于分治法。
		先取一个元素pivot作为基准，通过一趟排序将待排序列分为两部分。
		使前一部分的所有元素小于等于pivot,后一部分的所有元素大于等于pivot,则pivot放在了其最终位置上，这个过程为一趟快速排序。
		而后分别对两个子表重复上述过程，直至每部分内只有一个元素或者为空为止，即所有元素放在了其最终位置上。
*/


#include <stdio.h>
#define ElementType int
#define LENGTH 20
//分治
/*
int Partition(ElementType a[], int low, int high){		//划分算法
	ElementType pivot = a[low];
	while(low<high){
		while(high>low && a[high]>=pivot)high--;
		a[low] = a[high];
		while(high>low && a[low]<=pivot)low++;
		a[high] = a[low];
	}
	a[low] = pivot;
	return low;
}

void QuickSort(ElementType a[],int low,int high){

	if(low<high){
		int pivotPos = Partition(a,low,high);
		QuickSort(a,low,pivotPos-1);
		QuickSort(a,pivotPos+1,high);
	}
}
*/


//另一种两个指针一前一后逐步向后扫描的Partition()
int Partition(int a[], int low, int high)
{
	ElementType pivot = a[high];  //以最后一个元素为主元
	int one = low-1;              //one , two  来交换
	for(int two = low; two < high; ++two){
		if(a[two]>=pivot){
			++one;
			exchange(a[one],a[two]);
		}
	}
	exchange(a[one+1],a[high]);
	return one+1;
}


int Partition(int a[], int low, int high)
{
	ElementType pivot = a[low];
	while(low<high){									//while包含所有，>=
		while(low<high && a[high]>=pivot) high--;		//这里的条件老是忘
		a[low] = a[high];
		while(low<high && a[low]<=pivot) low++;
		a[high] = a[low];
	}
	a[low] = pivot;
	return low;
}

void QuickSort(int a[], int low, int high)
{
	if(low<high){
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
	QuickSort(a,0,LENGTH-1);
	for(int i=0;i<LENGTH;i++){
		printf("%d ", a[i]);
	}
	getchar();
	return 0;
}



