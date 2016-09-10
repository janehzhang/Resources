#include <stdio.h>
#include <string.h>
#include <stdlib.h>

//求最长公共子序列问题。 最优子结构，重叠子问题。自己分析。看着办。


/*
	计算最长公共子序列长度
	if X[i]==Y[j]  -->  C[i,j]=C[i-1,j-1]+1
	if X[i]!=Y[j]  -->  C[i,j]= max( C[i,j-1],C[i-1,j])
*/
int CountLSC(const char *X, const char *Y)
{
	int c[100][100];
	if(X == NULL || Y == NULL){
		return 0;
	}
	int xlen = strlen(X);
	int ylen = strlen(Y);
	for(int i=0; i<xlen; ++i){
		c[i][0] = 0;
	}
	for(int j=0; j<ylen; ++j){
		c[0][j] = 0;
	}
	c[0][0] = 0;
	for(int i=1; i<=xlen; ++i){
		for(int j=1; j<=ylen; ++j){
			if(X[i] == Y[j]){
				c[i][j] = c[i-1][j-1] + 1;
			}else{
				c[i][j] = c[i-1][j]>c[i][j-1]?c[i-1][j]:c[i][j-1];
			}
		}
	}
	return c[xlen][ylen];
}



/*
长度的问题我们已经解决了，这次要解决输出最长子序列的问题，
我们采用一个标记函数Flag[i,j],用来指示c[i,j]的值是由哪一个子问题的解达到的!!! 当

①：C[i,j]=C[i-1,j-1]+1  时 标记Flag[i,j]="left_up";  （左上方箭头） 我标记为1

②：C[i-1,j]>=C[i,j-1]   时 标记Flag[i,j]="up";     （上箭头）  我标记为2

③: C[i-1,j]<C[i,j-1]     时 标记Flag[i,j]="left";      （左箭头）  我标记为3

*/
void Subquence(int xlen, int ylen, int flag[][100], const char *X, int curr_len, int max_len){			//二维数组传参原来要这样
	//curr_len是当前长度，max_len是最长子序列的长度

	char result[max_len];		        //存放结果
	int count = 0;
	if(xlen==0 || ylen==0){

		for(int i=0; i<max_len; i++){
			printf("%s",result[i]);
		}
		count++;				//计算子序列有多少个
		return;
	}

	if(flag[ylen][xlen] == 1){				//ylen对应Y轴(竖)，xlen对应X轴(横)

		printf("%c, location is (%d,%d)\n",X[xlen-1],xlen,ylen);
		--curr_len;
		result[curr_len] = X[xlen-1];			//保存子序列字符到result数组,从后往前放，因为是倒序取的的
		Subquence(xlen-1, ylen-1, flag, X, curr_len, max_len);

	}else {

		if(flag[ylen][xlen] == 2){

			Subquence(xlen, ylen-1, flag, X, curr_len, max_len);

		}else{
			 if(flag[ylen][xlen]==3)
             {
                 Subquence(xlen-1, ylen, flag, X, curr_len, max_len);
             }
             else
             {
                 Subquence(xlen-1, ylen, flag, X, curr_len, max_len);
                 Subquence(xlen, ylen-1, flag, X, curr_len, max_len);
             }

			
		}

	}

}

char GetLSC(const char *X, const char *Y){
	int c[100][100];
	int flag[100][100];

	if(X == NULL || Y == NULL){
		return 0;
	}
	int xlen = strlen(X);
	int ylen = strlen(Y);
	for(int i=0; i<xlen; ++i){
		c[i][0] = 0;
	}
	for(int j=0; j<ylen; ++j){
		c[0][j] = 0;
	}
	c[0][0] = 0;
	
	for(int i=1; i<=ylen; ++i){					//i对应Y轴(竖)，j对应X轴(横)
		for(int j=1; j<=xlen; ++j){
			if(Y[i-1] == X[j-1]){
				c[i][j] = c[i-1][j-1] + 1;
				flag[i][j] = 1;					//指向左上的元素
			}else
				if(c[i-1,j] > c[i,j-1])
				{
					c[i][j] = c[i-1][j];
					flag[i][j] = 2;				//指向上面元素
				}else
				 	if(c[i-1,j] < c[i,j-1])
				 	{
						c[i][j] = c[i][j-1];
						flag[i][j] = 3;				//指向左面的元素
					}else
						if(c[i-1,j] == c[i,j-1])
						{		
							c[i][j] = c[i][j-1];	//或者c[i][j] = c[i-1][j];
							flag[i][j] = 4;
					    }
		}
	}

	printf("c[][]:\n");
	for(int i=0; i<=ylen;i++){					//算最长子序列长度的二维数组
		for(int j=0; j<=xlen; j++){
			printf("%3d",c[i][j]);
			if(j==xlen){
				printf("\n");
			}
		}
	}
	printf("LSC length is: %d\n",c[ylen][xlen]);

	printf("\nflag[][]:\n");
	for(int i=0; i<=ylen;i++){					//标记子序列字符的flag数组
		for(int j=0; j<=xlen; j++){
			printf("%3d",flag[i][j]);
			if(j==xlen){
				printf("\n");
			}
		}
	}

	Subquence(xlen,ylen,flag,X,c[ylen][xlen],c[ylen][xlen]);

}

int main(void){
	char X[] = "ABCBDAB";
	char Y[] = "BDCABA";
	//char X[] = "ABC";
	//char Y[] = "BC";
	int count =  GetLSC(X,Y);
	printf("%d\n", count);
	getchar();
	return 0;
}



//http://blog.chinaunix.net/uid-26548237-id-3374211.html

//动态规划是自底向上的策略, 比如上面的 GetLSC(X,Y) 填c[][]

//动态规划的变形备忘录则是自顶向下，也维护了记录子问题的表，但是填表的控制结构更像递归算法，比如上面的Subquence()填flag[][]