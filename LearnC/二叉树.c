#include <stdio.h>

#define LENGTH 10

typedef BitTreeNode{
	int data;
	BitTreeNode* lchild;
	BitTreeNode* rchild;
} BTreeNode, *pBTreeNode;



void insert(pBTreeNode root, pBTreeNode p){	//遍历到最下面就插入
	if(root == NULL){
		root = p;
	}else{
		pBTreeNode current = root;        //root！！！
		pBTreeNode parent;
		while(true){
			parent = current;
			if(current.data >= p.data){
				current = current.lchild;
				if(current == NULL){
					BTreeNode new;
					new.data = p.data;
					parent.lchild = new;
					return;
				}
			}else{
				current = current.rchild;
				if(current == NULL){
					BTreeNode new;
					new.data = p.data;
					parent.rchild = new;
					return;
				}
			}
		}

	}
}

init(int a[], int length)
{
	pBTreeNode tree,p;
	for(int i = 0; i < length; i++){
		p = (pBTreeNode) malloc( sizeof(BTreeNode));
		p->data = a[i];
		p->lchild = NULL;
		p->rchild = NULL;
		insert(tree,p);
	}
}

//后序遍历
void PostOrder(pBTreeNode T){
	Stack stack;
	
}


int main(){

	pBTreeNode root = init(a,LENGTH);
	PostOrder(root);

}