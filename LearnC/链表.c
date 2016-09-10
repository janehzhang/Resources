#include <stdio.h>
#include <string.h>
#include <stdlib.h>

/*
st.name="123";  非法。
因为常量字符串不能直接赋值给字符数组，因为st.name 地址是确定的，不能再改了。 
char name[10]="123"; 可以的，定义时，进行初始化字符数组。
*/

#define DATA_LENGTH 4     //学生数组长度

typedef struct StudentNode_tag{
	int number;
	char name[32];
	float score;
	struct StudentNode_tag* next;
} StudentNode, *pStudentNode;

StudentNode student[DATA_LENGTH] = {
		{001,"jiji",89},
		{002,"bobo",79},
		{003,"xiaoming",60},
		{004,"redlittle",100}
};

pStudentNode init()
{
	pStudentNode head =  (pStudentNode)malloc(sizeof(StudentNode));	//malloc动态内存分配
	pStudentNode pTrial = head;
	pTrial->next = NULL;
	printf("以下是链表内容:%s,%s,%s\n","number","name","score");
	for(int i = 0; i < DATA_LENGTH; i++){		//将student数据放入链表
		pStudentNode pNew =  (pStudentNode)malloc(sizeof(StudentNode));
		pNew->number = student[i].number;
		pNew->name = student[i].name;										//这里不行
		pNew->score = student[i].score;

		pTrial->next = pNew;
		pNew->next = NULL;
		pTrial = pNew;
	}
    printf("init finish\n");    
	return head;

}



int main(){

	//建立链表
	pStudentNode head = init();

	return 0;
}