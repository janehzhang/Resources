package BiTree;

import BiTree.init.BiTreeNode;
import BiTree.init.MyBiTree;

public class 判断两棵树是否相同 {
	
	public static void main(String[] args) {
		MyBiTree tree1  = new MyBiTree();
		MyBiTree tree2  = new MyBiTree();
		int[] data = {2,8,7,4,9,3,1,6,7,5};
		BiTreeNode root1 = tree1.init(data);
		BiTreeNode root2 = tree2.init(data);
		System.out.println(isEquals(root1, root2));
		
	}

	
	private static boolean isEquals(BiTreeNode root1, BiTreeNode root2) {
		
		if(root1 == null && root2 == null){
			return true;
		}
		if(root1.getData() == root2.getData()){
			//不考虑左右节点可以旋转的情况
			//return isEquals(root1.lChild, root2.lChild) && isEquals(root1.rChild, root2.rChild);
			//考虑左右节点可以旋转的情况
			return (isEquals(root1.lChild, root2.lChild) && isEquals(root1.rChild, root2.rChild)) ||
			(isEquals(root1.lChild, root2.rChild) && isEquals(root1.rChild, root2.lChild));
		}else{
			return false;
		}
		
	}

}
