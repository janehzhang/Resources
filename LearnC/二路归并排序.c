#include <stdio.h>
#include <stdlib.h>
#define ElementType int
#define Length 10

/*
ElementType *b = (ElementType*)malloc(Length*sizeof(ElementType));
void Merge(ElementType a[], int low, int mid, int high){
	//a[]的前后两端各自有序，将它们合并成一个有序表
	for(int k=low; k<high; k++){
		b[k] = a[k];
	}
	for(int i=0, j=mid+1, k=i; i<=mid && j<high; k++){
		if(b[i]<b[j]){
			a[k] = b[i++];
		}else{
			a[k] = b[j++];
		}
	}//for
	
	while(i<=mid){
		a[k++] = a[i++];
	}
	while(j<high){
		a[k++] = a[j++];
	}
}

void MergeSort(ElementType a[], int low, int high){
	if(low<high){
		int mid = (low+high)/2;
		MergeSort(a,low,mid-1);
		MergeSort(a,mid+1,high);
		Merge(a,low,mid,high);
	}
}
*/


ElementType *b = (ElementType*)malloc(Length*sizeof(ElementType));
void Merge(ElementType a[], int low, int middle, int high)
{
	for(int k = low; k < high; k++)
	{
		b[k] = a[k];
	}
	int i, j, k = 0;
	for(i = low, j = middle+1 ,k = i; i <= middle && j < high ; k++){
		if(b[low]>b[middle]){
			a[k++] = b[low];
		}
	}
	while(i<=middle) a[k++] = a[i++];
	while(j<=high) a[k++] = a[j++];
}


void MergeSort(ElementType a[], int low,int high)
{
	if(low<high){
		int middle = (low+high)/2;
		MergeSort(a,low,middle);
		MergeSort(a,middle+1,high);
		Merge(a,low,middle,high);
	}
}


int main()
{
	ElementType a[Length] = {12,2,43,23,33,2,1,65,8,5};
	MergeSort(a,0,Length);
	for(int i=0;i<Length;i++){
		printf("%d ", a[i]);
	}
	getchar();
	return 0;
}



