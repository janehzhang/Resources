#include <stdio.h>
#include <string.h>
#include <assert.h> 
//字符串转换成数字：扫描，然后总的就是当前加上之前*10;
//整数输入的可能有正负号
//非法检查: 输入为空?，输入不是数字的字符串?，溢出

#define MAX_INT  2147483647
#define MIN_INT  -(MAX_INT)-1
	//static const int MAX = (int)((unsigned)~0 >> 1);      
    //static const int MIN = -(int)((unsigned)~0 >> 1) - 1;

int StrToInt(const char* str)  
{ 

    int res = 0; 		// result  
    int i = 0; 			// index of str  
    int signal = '+';	 // signal '+' or '-'  
    int cur; 			// current digit  
  
    if (!str)  
        return 0;  
  
    // skip backspace  
   	while(str[i] == ' '){
   		i++;
   	}

    // skip signal  
    if (str[i] == '+' || str[i] == '-')  
    {  
        signal = str[i];  
        i++;  
    }  
  
    // get result  
    while (str[i] >= '0' && str[i] <= '9')  
    {  
        cur = str[i] - '0';  
  
        // judge overlap or not !!!!!!!!!!!!!!!!! 溢出就输出最大整数或者最小整数
        if ( (signal == '+') && ((res > MAX_INT/10)||(res == MAX_INT/10 && cur > MAX_INT%10 )) )  
        {  
            res = MAX_INT;  
            break;  
        }  
        else if ( (signal == '-') && ((res > (unsigned)MAX_INT/10)||(res == (unsigned)MAX_INT/10 && cur > (unsigned)MAX_INT%10 )) )  
        {  
            res = MIN_INT;  
            break;  
        }  
  
        res = res * 10 + cur;  
        i++;  
    }  
  
    return (signal == '-') ? -res : res;  
}  

int main(void){
	char str[] = "  10522545459";
	int n = StrToInt(str);
	printf("%d\n", n);
	getchar();
	return 0;
}