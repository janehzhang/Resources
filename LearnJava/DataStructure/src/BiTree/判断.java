package BiTree;

import BiTree.init.BiTreeNode;
import BiTree.init.MyBiTree;


public class 判断{

	private static boolean flag = true;
	private static int last = Integer.MIN_VALUE;
	
	//判断是否二叉排序树 。 不要以为左小于根，右大于根就行。
	//满足中序遍历是从小到大的
	private static boolean judge(BiTreeNode root) {
		if(root.lChild != null && flag){
			judge(root.lChild);
		}
		if(last > root.getData()){
			return false;
		}
		last = root.getData();
		if(root.rChild != null && flag){
			judge(root.rChild);
		}
		return flag;
	}
	
	
	
	
	//判断是否平衡二叉树。 求根节点的最小深度和最大深度，他们的差就是树中任意字树深度差的最大值
	private static boolean isBlance(BiTreeNode root){
		
		return flag;
	}
	
	int maxDepth(BiTreeNode root){
		if(root != null){
			return 0;
		}
		return 1 + max( maxDepth(root.lChild), maxDepth(root.rChild));
	}
	
	int minDepth(BiTreeNode root){
		if(root != null){
			return 0;
		}
		return 1 + min( maxDepth(root.lChild), maxDepth(root.rChild));
	}
	
	private int min(int x, int y) {
		return x>y?y:x;
	}

	private int max(int x, int y) {
		return x>y?x:y;
	}

	
	
	
	
	
	
	public static void main(String[] args) {
		MyBiTree biTree = new MyBiTree();
		int[] a = {2,8,7,4,9,3,1,6,7,5};
		BiTreeNode root = biTree.init(a);
		System.out.println(judge(root));
	}

	

}
