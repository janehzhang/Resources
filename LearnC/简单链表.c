#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#define DATALEN	10		//链表元素个数

int data[DATALEN] = {33,23,3,54,2321,
					 90,4355,99,14,2};		//用于建立链表

typedef struct Node_tag{
	int data;
	struct Node_tag* next;
} Node, *pNode;

//遍历链表
void TraverseList(pNode head)
{
	pNode p = head->next;
	printf("this is linkList:\n");
	while(p!=NULL){
		if(p->next==NULL){
			printf("%d\n",p->data);
		}else{
			printf("%d -->",p->data);
		}
		p = p->next;
	}
}

//根据预设数据建立链表
pNode init1()
{			
	pNode pNew,pTrail;			//新节点，尾部节点
	pNode head = (pNode)malloc(sizeof(Node));        //malloc动态内存分配
	pTrail = head;              //链表的入口地址赋给pTrail尾部节点这个家伙
	pTrail->next = NULL;			//最后一个节点置空

	for(int i = 0; i < DATALEN; i++){
		pNew = (pNode)malloc(sizeof(Node));
		pNew->data = data[i];
		pTrail->next = pNew;			//将最后一个节点的指针指向下一个新的节点
       	pNew->next = NULL;
       	pTrail = pNew;
	}
	printf("init finish!\n");
	TraverseList(head);
	return head;
}

//自己输入链表元素建链表
pNode init2()	
{
	pNode pNew,pTrail;			//新节点，尾部节点

	pNode head = (pNode)malloc(sizeof(Node));        //malloc动态内存分配

	pTrail = head;              //链表的入口地址赋给pTrail尾部节点这个家伙

	pTrail->next = NULL;			//最后一个节点置空

	pNew = (pNode)malloc(sizeof(Node));
	printf("please input number to build list(input zero will exit),pointer address:%d\n",pNew);
	scanf("%d", &pNew->data);
	pNew->next = NULL;
	while(pNew->data != 0){
       	pTrail->next = pNew;			//将最后一个节点的指针指向下一个新的节点
       	pNew->next = NULL;			//将新节点中的指针置为空！！！！！！！！处理好这个！！！！！！！！
   		pTrail = pNew;			 //将新节点接到表尾
   		pNew = (pNode)malloc(sizeof(Node));
   		printf("please input the number again,address is:%d\n",pNew);
		scanf("%d", &pNew->data);
	}
	free(pNew);  //申请到的没录入，所以释放掉    
    pNew=NULL;   //使指向空    
    pTrail->next = NULL; //到表尾了，指向空    
    printf("init finish!\n");
    TraverseList(head);
	return head;
}


//查找第index个节点
pNode Find(pNode head, int index)
{
	pNode p1 = head->next;
	if(index==0){
		return head;
	}
	while(p1 && (--index)){
		p1 = p1->next;
	}
	if(p1 && !index){
		return p1;
	}else{
		printf("cannot find the elem\n");
	}
	return NULL;
}

//增：在index后面增加一个elem节点
void Add(pNode head, int index, int elem)
{
	if(Find(head,index)==NULL){
		printf("cannot add element\n");
		return ;
	}
	pNode p2 = Find(head,index);
	pNode newNode = (pNode)malloc(sizeof(Node));
	newNode->data = elem;
	if(p2->next){
		newNode->next = p2->next;
		p2->next = newNode;
	}else{
		p2->next = newNode;
		newNode->next = NULL;
	}
	printf("insert elem %d after index:%d\n",elem,index);
	TraverseList(head);
}

//删：删掉第index个元素
void Delete(pNode head, int index)
{

	if(Find(head, index-1)==NULL){
		printf("cannot delete.\n");
		return ;
	}
	pNode p2 = Find(head, index-1);
	pNode p1 = p2->next;

	p2->next = p1->next;
	p1->next = NULL;
	free(p1);
	printf("delete element success\n");
	TraverseList(head);
}

//改：修改第index个元素变成elem
void Update(pNode head, int index, int element)
{
	if(Find(head, index)==NULL){
		printf("cannot delete.\n");
		return ;
	}
	pNode p2 = Find(head, index);
	p2->data = element;
	printf("delete element success\n");
	TraverseList(head);

}

//编程之美3.4 反转链表   用了三个指针
void ReverseLinkList(pNode head)
{
	bool flag = true;
	pNode p1,p2;
	p2 = head->next;
	p1 = p2->next;
	head->next = NULL;

	while(p1 != NULL)
	{
		pNode temp;
		temp = p1->next;
		if(flag==true){
			p2->next = NULL;
			flag = false;
		}
		p1-> next = p2;
		p2 = p1;
		p1 = temp;

	}
	head->next = p2;
	printf("reverse finish!\n");
	TraverseList(head);

}

int main(void)
{
	//建立链表
	pNode head = init1();
	
	//反转链表
	//ReverseLinkList(head);
	//查找链表元素
	//Find(head,5);
	//增
	//Add(head,1,10021);
	//删
	//Delete(head,10);
	//改
	//Update(head,10,100);

	getchar();
	printf("input any key to exit...");
	getchar();
	return 0;
}