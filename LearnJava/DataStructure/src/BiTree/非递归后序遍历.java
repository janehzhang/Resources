package BiTree;

import java.util.Stack;

import BiTree.init.BiTreeNode;
import BiTree.init.MyBiTree;

public class 非递归后序遍历 {

	public static void postOrder(BiTreeNode root){
		BiTreeNode parent = root , right = null;
		Stack<BiTreeNode> stack = new Stack<BiTreeNode>();
		
		while(parent != null || !stack.isEmpty()){
			if(parent != null){
				stack.push(parent);
				parent = parent.lChild;
			}else{
				parent = stack.peek();
				if(parent.rChild != null && parent.rChild != right){
					parent = parent.rChild;
					stack.push(parent);
					parent = parent.lChild;
				}else{
					parent = stack.peek();
					stack.pop();
					System.out.println(parent.getData()+" ");
					right = parent;
					parent = null;
				}
			}
		}
	}
	
	
	
	public static void main(String[] args) {
		MyBiTree tree  = new MyBiTree();
		int[] data = {2,8,7,4,9,3,1,6,7,5};
		BiTreeNode root = tree.init(data);
		
		postOrder(root);
		
	}

}
