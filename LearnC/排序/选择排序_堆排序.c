/*
选择排序：简单选择排序 ，堆排序

堆排序：先构造大根堆，再调整
*/

#include <stdio.h>
#define ElementType int
#define LENGTH 20

void AdjustDown(ElementType a[],int k,int length)			//将元素k向下进行调整为大根堆
{
	a[0] = a[k];								
	for(int i = k*2; i <= length; i *= 2){
		if(i<length && a[i]<a[i+1]){
			i++;
		}
		if(a[i] <= a[0]){
			break;
		}else{					//人家这里是else啊，没说要a[i]>a[i/2]啊，厉害！！！
			a[k] = a[i];		//调整节点到双亲节点上
			k = i;
		}
	}
	a[k] = a[0];     //被筛选节点的值放入最终位置
}


void BuildMax(ElementType a[], int length)		//0不存放数据
{
	for(int k = length/2; k>0; k--){		//从a[length/2]到a[1] ,反复调整堆
		AdjustDown(a,k,length);		
	}
}

void HeapSort(ElementType a[], int length)
{
	BuildMax(a,length);
	for(int i = length; i > 1; i--){
		int temp = a[i];
		a[i] = a[1];
		a[1] = temp;
		AdjustDown(a, 1 , i-1);			//把剩余的i-1个节点调整为堆
	}
}

int main()
{
	ElementType a[LENGTH+1] = {
	     0,2,43,23,33,2,1,65,8,5,
	    423,39,143543,11,999,32,76,777,76,12,21
	};
	HeapSort(a,LENGTH);
	for(int i=1;i<LENGTH+1;i++){
		printf("%d:%d  \n", i, a[i]);
	}
	getchar();
	return 0;
}