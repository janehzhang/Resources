#include<stdio.h>
#include<string.h>
#include<math.h>


int getContinuousNum(int N){

	int start;   //从start开始的n个连续自然数加起来可以构成N
	
	for(int n=3; n<=sqrt((float)N)*2; ++n){

		if((N-n*(n-1)/2)%n==0){
			start = (N-n*(n-1)/2)/n;
			if(start>0){
				printf(" %d could be continuously plus by:\n",N);
				for(int j=0; j<n; j++){
					printf("%4d", start+j);
				}
			}
			printf("\n");
		}

	}
	return 0;
}

int main(){

	int testNum = 100;
	getContinuousNum(testNum);

	//getchar();
	printf("%s\n", "press your keyboard to exit...");
	getchar();
	return 0;
}
