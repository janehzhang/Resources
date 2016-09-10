/*
折半查找
*/

#include <stdio.h>
#define ElementType int
#define LENGTH 20

int Binary_Search(ElementType a[], int low, int high, ElementType data)
{
	int mid;
	while(low <= high){
		mid = (low + high) / 2;
		if(data == a[mid]){
			return mid;
		}
		else if(data > a[mid]){
			low = mid + 1;
		}else{
			high = mid -1;
		}
	}
	return -1;
}

int main(){
	ElementType a[LENGTH] = {
	    12,2,43,23,33,2,1,65,8,5,
	    423,39,143543,11,0,32,76,777,76,12
	};
	int index = Binary_Search(a,0,LENGTH-1,777);
	printf("%d\n", index+1);
	getchar();
	return 0;

}


//对于循环有序数组怎么查找？{7，8，9，0，1，2，3，4，5，6}
int Binary_Search(ElementType a[], int low, int high, ElementType data)
{
	while(low <= high){
		int middle = (low + high) / 2;
		if( a[middle] == data){
			return middle;
		}
		else 
			if(a[low] <= a[middle]){
				if( data > a[middle]){
					low = middle + 1;
				}
				else if( data > a[low]){
					high = middle - 1;
				}
				else{
					low = middle + 1;
				}
			}
			else
				if( data < a[middle]){
					high = middle - 1;
				}
				else
					if( data < a[high]){
						low = middle + 1;
					}
				else{
					high = middle - 1;
				}

	}
	return -1;
}
