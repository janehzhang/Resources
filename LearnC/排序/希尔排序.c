/*
	插入排序: 直接插入排序 ，希尔排序。

	基本思想在于每次将一个待排序的记录，按其关键字大小插入到前面已经排好序的子序列中，直到全部记录插入完成。

	希尔排序：先将待排序列分隔成以增量步长d为间隔的子表，分别进行直接插入排序。
	 当整个表的元素都基本有序时，再对全体记录进行一次直接插入排序。

	所有记录分为d个组，所有距离为d的倍数的数据放在同一个组中，各组分别进行直接插入排序。
	重复上述过程直到d=1。即所有数据在同一组中，再进行直接插入排序。
	由于此时已经具有较好的局部有序性，故可以很快得到最终结果。


*/

#include <stdio.h>
#define ElementType int
#define LENGTH 20

void ShellSort2(ElementType a[],int length){

	int i,j,d;
	ElementType temp;

	for (d = length/2; d > 0; d = d/2)
	{
		for(i = d; i < length; i++){
			if(a[i] < a[i-d]){
				temp = a[i];
				for (j = i-d; j >= 0 && a[j]>temp ; j = j-d){
					a[j+d] = a[j];
				}
				a[j+d] = temp;
			}

		}
	}

}

void ShellSort3(ElementType a[],int length){

	int i,j,d;
	ElementType temp;

	for (d = length/2; d > 0; d = d/2)
	{
		for(i = d; i < length; i++){
			if(a[i] < a[i-d]){
				temp = a[i];
				for (j = i; j > 0 && a[j - d]>temp; j = j-d){
					a[j] = a[j-d];
				}
			}
			a[j] = temp;
		}
	}

}



void ShellSort(ElementType a[],int length){

	int i,j,d;
	ElementType temp;

	for(d=length/2; d>0; d=d/2){

		for(i=d; i<length; i++){
			temp = a[i];
			for(j=i-d; j>=0; j-=d){
				if(a[j]>temp){
					a[j+d] = a[j];
				}
				else{
					break;
				}
			}
			a[j+d] = temp;
		}
	}
}




int main()
{
	ElementType a[LENGTH] = {
	    12,2,43,23,33,2,1,65,8,5,
	    423,39,143543,11,0,32,76,777,76,12
	};
	ShellSort3(a,LENGTH);
	for(int i=0;i<LENGTH;i++){
		printf("%d ", a[i]);
	}
	getchar();
	return 0;
}