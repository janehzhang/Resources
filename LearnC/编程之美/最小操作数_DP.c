#include <stdio.h>
#include <string.h>
#include <stdlib.h>


int getMinValue(int a, int b, int c) {  
    if(a < b) {  
        if(a < c)  
            return a;  
        else   
            return c;  
    } else {  
        if(b < c)  
            return b;  
        else   
            return c;  
    }  
}  

//很多重复计算了
int CalculateStringDistance1(char* A, int AStart, int AEnd, char* B, int BStart, int BEnd)
{
	if(AStart > AEnd){             //边界条件，AStart处理完毕
		if(BStart > BEnd){
			return 0;
		}else{
			return BEnd - BStart + 1;
		}
	}
	if(BStart > BEnd){
		if(AStart > AEnd){
			return 0;
		}else{
			return AEnd - AStart + 1;
		}
	}
	if(A[AStart] == B[BStart]){
		return CalculateStringDistance(A, AStart+1, AEnd, B, BStart+1, BEnd);
	}else{
		int value1 = CalculateStringDistance(A, AStart+1, AEnd, B, BStart, BEnd);
		int value2 = CalculateStringDistance(A, AStart, AEnd, B, BStart+1, BEnd);
		int value3 = CalculateStringDistance(A, AStart+1, AEnd, B, BStart+1, BEnd);
		return getMinValue(value1,value2,value3)+1;
	}
}


int CalculateStringDistance2(char *A, char *B){

	int ALen = strlen(A);
	int BLen = strlen(B);
    int c[ALen+1][BLen+1];

    for(int i = 0; i <= ALen; i++)
         c[i][0] = i;
    for(int j = 0; j <= BLen; j++)
         c[0][j] = j;
 
    for(int i = 1; i <= ALen; i++)
    {
         for(int j = 1; j <= BLen; j++)
         {
             if((A[i - 1] == B[j - 1]))
             {
                 c[i][j] = c[i - 1][j - 1]; //不需要编辑操作
             }
             else
             {
                 int edIns = c[i][j - 1] + 1; //source 插入字符
                 int edDel = c[i - 1][j] + 1; //source 删除字符
                 int edRep = c[i - 1][j - 1] + 1; //source 替换字符
 
                 c[i][j] = getMinValue(edIns,edDel,edRep);
             }
         }
     }
 
     return c[ALen][BLen];

}



int main()
{
	char a[] = "efgwq";
    char b[] = "bvzww";
   
    int len_a = strlen(a);  
    int len_b = strlen(b);  
	
	//int distance = CalculateStringDistance1(a, 0, len_a - 1, b, 0, len_b - 1);  
   
    int distance = CalculateStringDistance2(a,b);		//动态规划


    printf("%d\n", distance);  
    getchar();
    return 0;

}
