/*
选择排序：简单选择排序 ，堆排序

简单选择排序：
*/

#include <stdio.h>
#define ElementType int
#define LENGTH 20

/*
void SelectSort(ElementType a[], int length)
{
	for(int i = 0; i < length-1; i++){
		int min = i;							//记录最小元素的位置
		for(int j = i+1; j < length; j++){		//每次选出最小元素
			if(a[j]<a[min]){
				min = j;						//更新最小元素位置
			}
		}
		if(min != i){
			ElementType temp = a[min];
			a[min] = a[i];
			a[i] = temp;
		}
	}
	
}
*/

void SelectSort(ElementType a[], int length)
{
	for(int i=0 ; i<length-1; i++){
		int index = i;
		for(int j=i+1; j<length; j++){
			if(a[j] < a[index])
				index = j;
		}
		if(index != i){
			ElementType temp = a[index];
			a[index] = a[i];
			a[i] = temp;
		}
	}
}


int main()
{
	ElementType a[LENGTH] = {
	    12,2,43,23,33,2,1,65,8,5,
	    423,39,143543,11,0,32,76,777,76,12
	};
	SelectSort(a,LENGTH);
	for(int i=0;i<LENGTH;i++){
		printf("%d:%d  \n", i+1,a[i]);
	}
	getchar();
	return 0;
}