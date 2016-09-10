#include<stdio.h>

int MaxSubSum(int *array,int left,int right){

	int sum=0;
	int i;
	if(left==right){
		if(array[left]>0){
			sum = array[left];
		}else{
			sum = 0;
		}
	}else{

		int center = (left+right)/2;
		int leftsum = MaxSubSum(array, left,center);
		int rightsum = MaxSubSum(array, center+1, right);

		int s1 = 0;
		int lefts = 0;
		for(i=center; i>=left; i--){
				lefts = lefts+array[i];
				if(lefts>s1){
					s1 = lefts;
				}
		}
		int s2 = 0;
		int rights = 0;
		for(i=center+1; i<=right; i++){
				rights = rights+array[i];
				if(rights>s2){
					s2 = rights;
				}
		}

		sum = s1+s2;

		if(sum<leftsum){
			sum = leftsum;
		}
		if(sum<rightsum){
			sum = rightsum;
		}
	}

	return sum;
}


int main()
{
	int array[4] = {7,2,-8,6};
	int sum = MaxSubSum(array,0,3);
	printf("%d", sum);
	return 0;
}


