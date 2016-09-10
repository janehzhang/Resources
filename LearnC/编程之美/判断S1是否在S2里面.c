#include <stdio.h>
#include <string.h>
#include <stdlib.h>

//问题：判断s2是否包含在s1移位得到的字符串当中，  还可以用KMP算法

int rotate_conbine(char *src,char *des){

	int len = strlen(src);		
	char *temp = (char *)malloc((2*len+1)*sizeof(char));
	char *p = temp;
	char *s = src;
	while(*s != '\0'){
		*p = *s;
		p++;
		s++;
	}
	s = src;  
    while(*s != '\0'){  
        *p = *s;  
        p++;  
        s++;  
    }  
    p++;
    *p = '\0';   	//这个真没想到！
    if(strstr(temp, des) != NULL)  
        return 1;  
    else  
        return 0; 
}

/*
int rotate_conbine2(string src, string des){

	string tmp = src;	//字符串直接相加啊
    src=src+tmp;
 	if(strstr(src.c_str(),des.c_str())==NULL)
    {
        return 0;   
    }
    return 1; 

}
*/

int main(){
	char src[] = "AABBDD";
	char des[] = "CDAA";
	if(rotate_conbine(src,des)){
		printf("included");
	}else{
		printf
		("excluded");
	}
	getchar();
	return 0;
}


/*
解法三：
我们的想法是:在s1后面"虚拟"地接上一个s1，这个"虚拟的s1"并不占空间，但是仍然按照解法2的思路进行。
只要把s1的最后一个元素，再指回s1的第一个元素即可。
这可以用取模运算实现。比如，元素s1[(d1+i) mod d1]其实就是那个“虚拟的s1”的第i个元素，这里 0<=i<=d1-1, d1是字符串s1的长度。
同理，指针也可以实现类似功能。
解法三的优点：
1. 字符串长度较大时，效率依然较好；
2.不需要申请额外空间存储第二个s1

#include<iostream>
using namespace std;

int ptr_contain(char *src, char *des){
    char *p = NULL;
    char *q = NULL;
    char *r = NULL;
    p = q = src;
    r = des;
    char *tmp = NULL;
    while(*p != '\0' )
    {
        while (*p != *r)
            p++;
        tmp = p;
        tmp++;
        while(*(++r) != '\0')
        {
            if(*(++p) == '\0')
                p = q;
            if(*r != *p)
            { 
                r = des;
                p = tmp;
                break;
            }
        }
        if(*r == '\0')
            return 1;
    }
    return 0;
}

*/