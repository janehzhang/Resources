/*
	交换排序：冒泡排序，快速排序。
	
	根据序列中两个元素的比较结果来对换这两个记录在序列中的位置。

	冒泡排序：假设序列长为n，从后往前或者从前往后两两比较相邻元素的值。
		若为逆序则交换，直到序列比较完。这样则为一趟冒泡，结果将最小的元素交换到待排序列的第一个位置
		（关键字最小的元素如气泡一样逐渐往上漂浮直至水面，这就是冒泡排序名字的由来。）
		下一趟排序时，前一趟排序确定的最小元素不再参与比较。这样就减少了一个元素。

	空间复杂度O(1),最坏时间复杂度O(n^2),最好时间复杂度O(n),平均时间复杂度O(n^2)。
		

*/


#include <stdio.h>
#define ElementType int
#define LENGTH 20

void BubbleSort(int a[], int n)
{
	ElementType temp;
	for(int i = 0; i < n-1; i++){
		int flag = 0;					//这个flag都是要加的
		for (int j = n-1; j > i; j--)
		{
			if(a[j]<a[j-1]){
				flag = 1;
				temp = a[j-1];
				a[j-1] = a[j];
				a[j] = temp;
			}
		}
		if(!flag){				//本趟遍历后没有发生交换，说明表已经有序
			return;
		}

	}
}

int main(){
	ElementType a[LENGTH] = {
	    12,2,43,23,33,2,1,65,8,5,
	    423,39,143543,11,0,32,76,777,76,12
	};
	BubbleSort(a,LENGTH);
	for(int i=0;i<LENGTH;i++){
		printf("%d ", a[i]);
	}
	getchar();
	return 0;
}