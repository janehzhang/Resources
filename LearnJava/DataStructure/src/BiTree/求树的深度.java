package BiTree;

import BiTree.init.BiTreeNode;
import BiTree.init.MyBiTree;

//由于需要求出左右子树的深度，才能求出根节点的深度，所以本质上是二叉树的后序遍历

public class 求树的深度 {
	
	private static int getDepth(BiTreeNode root) {
		
		return -1;
	}
	
	
	/*
	public static int getDepth(BiTreeNode root) {
		int left,right = 0;
		if(root != null){
			left = getDepth(root.lChild) + 1;
			right = getDepth(root.rChild) + 1;
			return (left > right) ? left : right;
		}else{
			return -1;  //这个是因为调用时要加1
		}
	}
	*/
	
	public static void main(String[] args) {
		MyBiTree tree  = new MyBiTree();
		int[] data = {2,8,7,4,9,3,1,6,7,5};
		BiTreeNode root = tree.init(data);
		
		System.out.println(getDepth(root));
		
	}

	
	
	
}
