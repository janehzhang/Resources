#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#define TELLENGTH  2

int RecursiveSearch(int* number, int* answer, int index, int n){

	char c[10][10] = 	//0-9键盘对应的字符
	{
		"",
		"",
		"ABC",
		"DEF",
		"GHI",
		"JKL",
		"MNO",
		"PQRS",
		"TUV",
		"WXYZ"
	};
	int total[10]={0,0,3,3,3,3,3,4,3,4};
	if(index == n){
		for(int i=0; i<n; i++){
			printf("%c",c[number[i]][answer[i]]);
		}
		printf("\n");
		return 0;
	}
	for(answer[index]=0; answer[index]<total[number[index]]; answer[index]++){
		RecursiveSearch(number, answer, index+1, n);
	}
}

int main(){
	int tel[TELLENGTH] = {2,3,3};				//号码
	int answer[TELLENGTH] = {0,0,0};			//每个按键中字符位置，到时从零开始遍历。answer[0]=2;指第一个号码的第二个字符
	RecursiveSearch(tel,answer,0,TELLENGTH);
	getchar();
	return 0;
}