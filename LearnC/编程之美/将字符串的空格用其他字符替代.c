#include <stdio.h>
#include <string.h>
#include <assert.h> 

//将所有空格用'%20'替换。
//先遍历str统计空格数量，再根据空格数量开辟一个新空间用于存储替换后的字符串。
//从字符串末尾!!!!开始遍历，没遇到一个空格就用'%20'替换。

void ResplaceFun(char *str)
{
	int count = 0;
	assert(str!=NULL);

	int length = strlen(str);
	for(int i =0; i < length; i++){
		if(str[i] == ' '){
			count ++;
		}
	}
	int newLength = length + count*2;
	str[newLength] = '\0';						//注意显示地添加'\0'
	for(int i = length-1; i >= 0; i--){
		if(str[i] == ' '){
			str[newLength-1] = '0';
			str[newLength-2] = '2';
			str[newLength-3] = '%';
			newLength = newLength - 3;
		}
		else{
			str[newLength-1] = str[i];
			newLength = newLength - 1;
		}
	}
	printf("%d\n", count);
	printf("%s\n", str);
}

int main(void){
	char str[] = "we are family";
	ResplaceFun(str);
	getchar();
	return 0;
}