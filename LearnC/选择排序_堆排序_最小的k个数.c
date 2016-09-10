/*
问题：输入k个数，输出其中最小的k个。

用堆排序：特别适合用于海量数据处理

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


void BuildMax(ElementType a[], int length)		
{
	for(int k = length/2; k>0; k--){		//从a[length/2]到a[1] ,反复调整堆
		AdjustDown(a,k,length);		
	}
}

void HeapSortK(ElementType a, int length; int k){	    //输入length个数，输出其中最小的k个。

	ElementType b[k+1];  //从a中读入k个数，b[0]不存放
	for(int i=1; i<k+1; i++){
		b[i] = a[i];
	}
	BuildMax(b,k);
	for(int i = k; i<length; i++){
		if(a[i]>b[1]){
			continue;
		}else{
			b[1] = a[i];
			AdjustDown(b,1,k);
		}
	}
	for(int i=1;i<=k;i++){
		printf("%d:%d  \n", i, b[i]);
	}
}


int main()
{
	ElementType a[LENGTH+1] = {
	     0,2,43,23,33,2,1,65,8,5,
	    423,39,143543,11,999,32,76,777,76,12,21
	};
    HeapSortK(a,LENGTH,5);
	getchar();
	return 0;
}