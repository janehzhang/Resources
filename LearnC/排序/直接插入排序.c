/*
	插入排序: 直接插入排序 ，希尔排序。

	基本思想在于每次将一个待排序的记录，按其关键字大小插入到前面已经排好序的子序列中，直到全部记录插入完成。
*/
/*
	直接插入排序
*/

#include <stdio.h>
#define ElementType int       /*带分号的定义会成为type会对于 int;  这是一种很常见的错误*/ 
#define LENGTH 20

void InsertSort2(ElementType a[],int length)
{
	ElementType temp = 0;
	for(int i=1; i<length; i++){
		temp = a[i];
		int j = i;
		if(a[i]<a[i-1]){
			for( ; (j>0) && (a[j-1] > temp); j--){		//判断不可缺少
					a[j] =a[j-1];
	
			}
		}
		a[j] = temp;
	}
}
 
int main()
{
	ElementType a[LENGTH] = {
	    12,2,43,23,33,2,1,65,8,5,
	    423,39,143543,11,0,32,76,777,76,12
	};
	InsertSort2(a,LENGTH);
	for(int i=0;i<LENGTH;i++){
		printf("%d:%d  \n", i+1,a[i]);
	}
	getchar();
	return 0;
}