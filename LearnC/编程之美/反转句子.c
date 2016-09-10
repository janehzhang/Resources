#include <stdio.h>
#include <string.h>
#include <stdlib.h>

//Today is Friday! -->  Friday! is Today

void Reverse(char *begin, char *end)
{
	if(begin == NULL || end == NULL)return;
	while(begin < end){
		char temp = *end;
		*end = *begin;
		*begin = temp;
		begin ++;
		end --;
	}
}


char* Reverse_pDatatence(char *pData){

	if(pData == NULL) return NULL;
	char *pBegin = pData; char *pEnd = pData;
	while(*pEnd != '\0'){
		pEnd ++;
	}
	pEnd --;	//去掉空格
	Reverse(pBegin,pEnd);				//先反转这个句子
	pEnd = pData;
	while(*pBegin != '\0'){
		if(*pBegin == ' '){
			pBegin ++;
			pEnd ++;
			continue;
		}
		else if(*pEnd == ' ' || *pEnd == '\0'){		//再反转单词
			Reverse(pBegin, --pEnd);		//!!!!
			pBegin = ++pEnd;
		}else{
			pEnd ++;
		}

	}
	printf("%s\n",pData);		//作为字符串打印

	return pData;
	
}


int main(void)
{
	char str[] = "Today is Friday!";
	char *data = Reverse_pDatatence(str);
	printf("%s\n", data);				
	getchar();
	return 0;
}