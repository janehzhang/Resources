package BiTree;

import BiTree.init.BiTreeNode;
import BiTree.init.MyBiTree;

//未完成

//由先序遍历和中序遍历 推断  后序遍历  来构建树

public class 由先序遍历和中序遍历构建树{
	
	
	public static BiTreeNode initTree(int[] pre, int s1, int e1, int[] in, int s2, int e2){
		
		if(s1 > e1 || s2 > e2){
			return null;
		}
		int rootData = pre[s1];
		BiTreeNode head = new BiTreeNode(rootData);
		
		//找到节点所在位置
		int rootIndex = 0;
		for(int i = s2; i <= e2; i++){
			if(in[i] == rootData){
				rootIndex = i;
				break;
			}
		}
		int offest = rootIndex - s2 -1;
		//构造左子树
		BiTreeNode lNode =  initTree(pre, s1 + 1, s1 + 1 + offest, in, s2, s2 + offest);
		BiTreeNode rNode =  initTree(pre, s1 + offest + 2, e1, in, offest +1, e2);
		head.lChild = lNode;
		head.rChild = rNode;
		return head;
		
	}
	public static void main(String[] args){
		
		int[] pre = {2,1,8,7,4,3,6,5,7,9};  //先序遍历
		int[] in = {2,1,8,7,4,3,6,5,7,9};	//中序遍历
		BiTreeNode head = initTree(pre, 0, pre.length-1, in, 0, in.length-1);
		MyBiTree tree = new MyBiTree(head);
		tree.postOrder(head);
	}
	
}
